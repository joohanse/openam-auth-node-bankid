package org.forgerock.auth.node.bankid.client;

import org.forgerock.auth.node.bankid.BankIDNode;
import org.forgerock.util.thread.listener.ShutdownManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class BankIDClientRegistry {

    private final ConcurrentMap<String, BankIDClient> cache = new ConcurrentHashMap<>();


    @Inject
    public BankIDClientRegistry(ShutdownManager shutdownManager) {
        //Register listener to tidy up resulting threads on shutdown
        shutdownManager.addShutdownListener(() -> {
            for (BankIDClient client : cache.values()) {
                try {
                    client.close();
                } catch (IOException e) {
                    // Abandon attempt to close the handler, the handler may have already been closed.
                    //DEBUG.message("Unable to close the HttpClientHandler", e);
                }
            }
        });
    }

    public BankIDClient get(BankIDNode.BankIDConfig config) {
        String id = clientId(config);
        BankIDClient client = cache.get(id);
        if (client == null) {
            synchronized (BankIDClientRegistry.class) {
                client = cache.get(id);
                if (client == null) {
                    client = new BankIDClient(config);
                    cache.put(id, client);
                }
            }
        }
        return client;
    }

    private String clientId(BankIDNode.BankIDConfig config) {
        return String.format("%d:%d:%d",
                config.clientPemPublicKey().hashCode(),
                config.clientPemSecretKey().hashCode(),
                Arrays.hashCode(config.clientPemSecretKeyPassword()));
    }
}
