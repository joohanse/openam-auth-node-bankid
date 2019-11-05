package org.forgerock.auth.node.bankid.client;

import org.forgerock.auth.node.bankid.BankIDNode;
import org.forgerock.auth.node.bankid.client.env.BankIDEnvironment;
import org.forgerock.util.Pair;
import org.testng.annotations.Test;

public class BankIDClientTest {


//    private static final Provider BC_PROVIDER = new BouncyCastleProvider();
//
//    @BeforeClass
//    public void addProvider() {
//        Security.addProvider(BC_PROVIDER);
//    }
//
//    @AfterClass
//    public void removeProvider() {
//        Security.removeProvider(BC_PROVIDER.getName());
//    }


    @Test(enabled = false)
    public void testAuth() throws Exception {
        BankIDClient client = new BankIDClient(new Config());
        Pair<String, String> pair = client.auth("194.168.2.25", "198307172349", null);

        System.out.println("bankid:///?autostarttoken=" + pair.getSecond());

        BankIDClient.HintCode state;
        do {
//            System.out.println("Press Enter to continue");
//            try {
//                System.in.read();
//            } catch (Exception e) {
//            }
            Thread.sleep(5000);

            state = client.collect(pair.getFirst(), null, false);
        } while (state.pending());
    }


    private class Config implements BankIDNode.BankIDConfig {
        @Override
        public BankIDNode.EnvironmentType environmentType() {
            return BankIDNode.EnvironmentType.TEST;
        }

        @Override
        public String clientPemSecretKey() {
            return BankIDEnvironment.testKeyPairSecret;
        }

        @Override
        public char[] clientPemSecretKeyPassword() {
            return "qwerty123".toCharArray();
        }

        @Override
        public String clientPemPublicKey() {
            return null;
        }
    }
}