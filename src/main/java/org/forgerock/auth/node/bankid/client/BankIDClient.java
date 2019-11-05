package org.forgerock.auth.node.bankid.client;

import com.sun.identity.authentication.spi.AuthLoginException;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.shared.configuration.SystemPropertiesManager;
import com.sun.identity.shared.encode.Base64;
import org.forgerock.auth.node.bankid.BankIDNode;
import org.forgerock.auth.node.bankid.client.env.BankIDEnvironment;
import org.forgerock.http.handler.HttpClientHandler;
import org.forgerock.http.protocol.Request;
import org.forgerock.http.protocol.Response;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.authentication.modules.common.mapping.DefaultAccountProvider;
import org.forgerock.services.context.RootContext;
import org.forgerock.util.Function;
import org.forgerock.util.Options;
import org.forgerock.util.Pair;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.iplanet.am.util.SecureRandomManager.getSecureRandom;
import static com.sun.identity.authentication.spi.AMLoginModule.getAMIdentityRepository;
import static com.sun.identity.shared.Constants.SYSTEM_PROXY_ENABLED;
import static java.util.stream.Collectors.toMap;
import static org.forgerock.http.handler.HttpClientHandler.OPTION_KEY_MANAGERS;
import static org.forgerock.http.handler.HttpClientHandler.OPTION_PROXY_SYSTEM;
import static org.forgerock.http.handler.HttpClientHandler.OPTION_TRUST_MANAGERS;
import static org.forgerock.json.JsonValue.array;
import static org.forgerock.json.JsonValue.field;
import static org.forgerock.json.JsonValue.fieldIfNotNull;
import static org.forgerock.json.JsonValue.json;
import static org.forgerock.json.JsonValue.object;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.REALM;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;
import static org.forgerock.openam.auth.nodes.oauth.SocialOAuth2Helper.ATTRIBUTES_SHARED_STATE_KEY;
import static org.forgerock.openam.auth.nodes.oauth.SocialOAuth2Helper.USER_INFO_SHARED_STATE_KEY;
import static org.forgerock.openam.auth.nodes.oauth.SocialOAuth2Helper.USER_NAMES_SHARED_STATE_KEY;


public class BankIDClient {
    static final String USER_STATUS = "inetuserstatus";
    static final String USER_PASSWORD = "userPassword";
    static final String ACTIVE = "Active";

    private final HttpClientHandler httpClientHandler;
    private final BankIDEnvironment environment;

    BankIDClient(BankIDNode.BankIDConfig config) {
        environment = BankIDEnvironment.newInstance(config.environmentType());

        try {
            KeyStore ks = SSLFactory.createKeystore(
                    config.clientPemSecretKeyPassword(), config.clientPemSecretKey(),
                    environment.getCertificateChain(config.clientPemPublicKey()));

            final Options options = Options.defaultOptions();
            options.set(OPTION_PROXY_SYSTEM, SystemPropertiesManager.getAsBoolean(SYSTEM_PROXY_ENABLED, false));

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, config.clientPemSecretKeyPassword());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            options.set(OPTION_KEY_MANAGERS, kmf.getKeyManagers());
            options.set(OPTION_TRUST_MANAGERS, tmf.getTrustManagers());


            httpClientHandler = new HttpClientHandler(options);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void close() throws IOException {
        httpClientHandler.close();
    }

    public Pair<String, String> auth(String endUserIp, String personalNumber, Map<String, Object> requirement) throws Exception {
        Request request = new Request();
        request.setMethod("POST");
        request.setUri(environment.getAuthEndpoint());
        request.setEntity(object(field("endUserIp", endUserIp),
                fieldIfNotNull("personalNumber", personalNumber),
                fieldIfNotNull("requirement", requirement)));
        request.getHeaders().put("Content-Type", "application/json");

        /*
            {
                "orderRef":"131daac9-16c6-4618-beb0-365768f37288",
                "autoStartToken":"7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6"
            }
         */
        Response response = httpClientHandler.handle(new RootContext(), request).getOrThrow();
        if (response.getStatus().isSuccessful()) {
            JsonValue body = new JsonValue(response.getEntity().getJson());
            return Pair.of(
                    body.get("orderRef").required().asString(),
                    body.get("autoStartToken").required().asString());
        }
        throw new Exception("Error");
    }

    public HintCode collect(String orderRef, TreeContext context, Boolean provisionUser) throws Exception {
        Request request = new Request();
        request.setMethod("POST");
        request.setUri(environment.getCollectEndpoint());
        request.setEntity(object(field("orderRef", orderRef)));
        request.getHeaders().put("Content-Type", "application/json");

        /*
            {
                "orderRef":"131daac9-16c6-4618-beb0-365768f37288",
                "status":"failed",
                "hintCode":"userCancel"
            }
         */
        Response response = httpClientHandler.handle(new RootContext(), request).getOrThrow();
        if (response.getStatus().isSuccessful()) {
            JsonValue body = new JsonValue(response.getEntity().getJson());
            HintCode status = body.get("status").as((Function<JsonValue, HintCode, Exception>) jsonValue -> {
                try {
                    return HintCode.valueOf(jsonValue.asString());
                } catch (IllegalArgumentException e) {
                    return HintCode.failed;
                }
            });
            if (status.success()) {
                JsonValue completionData = body.get("completionData").get("user");
                String personalNumber = completionData.get("personalNumber").asString();
                String name = completionData.get("name").asString();
                String givenName = completionData.get("givenName").asString();
                String surname = completionData.get("surname").asString();


                context.sharedState.put(USERNAME, personalNumber);


                String realm = context.sharedState.get(REALM).asString();
                Map<String, Set<String>> attributes = new HashMap<>();
                attributes.put("uid", Collections.singleton(personalNumber));
                attributes.put("iplanet-am-user-alias-list", Collections.singleton(personalNumber));

                if (getUser(realm, attributes) == null) {
                    if (provisionUser) {
                        //#1: Just Provision Here
                        attributes = new HashMap<>();
                        attributes.put("uid", Collections.singleton(personalNumber));
                        attributes.put("iplanet-am-user-alias-list", Collections.singleton(personalNumber));
                        attributes.put("sn", Collections.singleton(surname));
                        attributes.put("givenName", Collections.singleton(givenName));
                        attributes.put("cn", Collections.singleton(name));
                        attributes.put(USER_PASSWORD, Collections.singleton(getRandomData()));
                        attributes.put(USER_STATUS, Collections.singleton(ACTIVE));
                        provisionUser(realm, attributes);
                    } else {
                        //#2: Provision Dynamic Node
                        context.sharedState.put(USER_INFO_SHARED_STATE_KEY,
                                json(object(
                                        field(ATTRIBUTES_SHARED_STATE_KEY, object(
                                                field("uid", array(personalNumber)),
                                                field("sn", array(surname)),
                                                field("givenName", array(givenName)),
                                                field("cn", array(name)),
                                                field("iplanet-am-user-alias-list", array(personalNumber))
                                        )),
                                        field(USER_NAMES_SHARED_STATE_KEY, object(
                                                field("iplanet-am-user-alias-list", array(personalNumber))))
                                )));
                    }
                }

            }
            return status;
        }
        return HintCode.failed;
    }

    private Map<String, ArrayList<String>> convertToMapOfList(Map<String, Set<String>> mapToConvert) {
        return mapToConvert.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
    }

    public enum HintCode {
        outstandingTransaction(0),
        noClient(0),
        started(0),
        userSign(0),
        expiredTransaction(-1),
        certificateErr(-1),
        userCancel(-1),
        cancelled(-1),
        startFailed(-1),
        //Status
        pending(0),
        failed(-1),
        complete(1);

        private final int family;

        HintCode(int family) {
            this.family = family;
        }

        public boolean failed() {
            return this.family == -1;
        }

        public boolean pending() {
            return this.family == 0;
        }

        public boolean success() {
            return this.family == 1;
        }
    }


    private AMIdentity getUser(String realm, Map<String, Set<String>> userNames) {

        AMIdentity userIdentity = null;
        if ((userNames != null) && !userNames.isEmpty()) {
            userIdentity = new DefaultAccountProvider().searchUser(
                    getAMIdentityRepository(realm), userNames);

        }
        return userIdentity;
    }

    /**
     * Provisions a user with the specified attributes.
     *
     * @param realm      The realm.
     * @param attributes The user attributes.
     * @return The name of the created user.
     * @throws AuthLoginException If an error occurs creating the user.
     */
    public String provisionUser(String realm, Map<String, Set<String>> attributes)
            throws AuthLoginException {
        AMIdentity userIdentity = new DefaultAccountProvider().provisionUser(getAMIdentityRepository(realm), attributes);
        return userIdentity.getName();
    }

    /**
     * Generates a string of 20 random bytes.
     *
     * @return the Base64 encoded random string.
     * @throws NodeProcessException Thrown if an error occurs while generating a random string
     */
    public String getRandomData() throws NodeProcessException {
        byte[] pass = new byte[20];
        try {
            getSecureRandom().nextBytes(pass);
        } catch (Exception e) {
            throw new NodeProcessException("Error while generating random data", e);
        }
        return Base64.encode(pass);
    }
}
