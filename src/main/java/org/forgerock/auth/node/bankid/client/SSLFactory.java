package org.forgerock.auth.node.bankid.client;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Factory methods to create SSL and JSE configuration objects
 */
public class SSLFactory {

    private static final Provider BC_PROVIDER = new BouncyCastleProvider();

    /**
     * Reads a base64-format PEM key and returns a Java KeyPair for it.
     *
     * @param privateKey PEM-encoded private key
     */
    public static KeyPair readKeyPair(String privateKey) {
        try (StringReader keyReader = new StringReader(privateKey);
             PEMParser pemReader = new PEMParser(keyReader)) {

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getKeyPair((PEMKeyPair) pemReader.readObject());
        } catch (IOException x) {
            // Shouldn't occur, since we're only reading from strings
            throw new RuntimeException(x);
        }
    }

    /**
     * Reads a base64-format PEM key and returns a Java PrivateKey for it.
     *
     * @param privateKey PEM-encoded private key
     */
    private static PrivateKey readPrivateKey(String privateKey, char[] password) {
        try (StringReader keyReader = new StringReader(privateKey);
             PEMParser pemReader = new PEMParser(keyReader)) {

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BC_PROVIDER);
            Object keyPair = pemReader.readObject();
            if (keyPair instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) keyPair);
            } else if (keyPair instanceof PKCS8EncryptedPrivateKeyInfo) {
                InputDecryptorProvider decryptionProv =
                        new JceOpenSSLPKCS8DecryptorProviderBuilder().setProvider(BC_PROVIDER).build(password);
                PrivateKeyInfo keyInfo = ((PKCS8EncryptedPrivateKeyInfo) keyPair).decryptPrivateKeyInfo(decryptionProv);
                return converter.getPrivateKey(keyInfo);
            } else {
                return converter.getPrivateKey(((PEMKeyPair) keyPair).getPrivateKeyInfo());
            }
        } catch (IOException x) {
            // Shouldn't occur, since we're only reading from strings
            throw new RuntimeException(x);
        } catch (OperatorCreationException | PKCSException e) {
            throw new RuntimeException("failed to decrypt protected keypair ", e);
        }
    }

    /**
     * Creates an in-memory KeyStore by reading a certificate chain and private/public keypair from two base64-format PEM strings.
     *
     * @param password         Password to encrypt the keystore with
     * @param privatekey       PEM-encoded private key
     * @param certificateChain PEM-encoded certificate(s), concatenated
     */
    static KeyStore createKeystore(char[] password, String privatekey, String certificateChain) throws KeyStoreException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS");
        PrivateKey privateKey = readPrivateKey(privatekey, password);

        try (InputStream caStream = new ByteArrayInputStream(certificateChain.getBytes())) {

            // Initialize empty keystore
            ks.load(null, password);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ks.store(buffer, password);
            ks.load(new ByteArrayInputStream(buffer.toByteArray()), password);

            // store certificates
            Certificate[] chain = CertificateFactory.getInstance("X509").generateCertificates(caStream).toArray(new Certificate[0]);
            for (Certificate cert : chain) {
                String caAlias = ((X509Certificate) cert).getSubjectX500Principal().getName();
                ks.setCertificateEntry(caAlias, cert);

            }

            ks.setKeyEntry("key", privateKey, password, chain);
        } catch (IOException e) {
            // Shouldn't occur, since we're only reading from strings
            throw new RuntimeException(e);
        }
        return ks;
    }

    /**
     * Create an SSL context based on a KeyStore
     *
     * @param ks       A keystore with a private key and certificate chain.
     * @param password the password for the keystore.
     */
    static SSLContext createSSLContext(KeyStore ks, char[] password) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));
        return sslContext;
    }
}
