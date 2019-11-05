package org.forgerock.auth.node.bankid;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.authentication.callbacks.ScriptTextOutputCallback;
import com.sun.identity.sm.RequiredValueValidator;
import org.forgerock.auth.node.bankid.client.BankIDClient;
import org.forgerock.auth.node.bankid.client.BankIDClientRegistry;
import org.forgerock.auth.node.bankid.client.env.BankIDEnvironment;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.AbstractDecisionNode;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.sm.annotations.adapters.Password;
import org.forgerock.openam.utils.qr.GenerationUtils;
import org.forgerock.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.TextOutputCallback;
import java.util.ResourceBundle;

import static org.forgerock.openam.auth.node.api.Action.send;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

/**
 * The Swedish BankID node. Contains pre-populated configuration for TEST.
 */
@Node.Metadata(outcomeProvider = AbstractDecisionNode.OutcomeProvider.class,
        configClass = BankIDNode.BankIDConfig.class)
public class BankIDNode extends AbstractDecisionNode {

    private static final String BUNDLE = "org/forgerock/openam/auth/nodes/BankIDNode";
    private final Logger logger = LoggerFactory.getLogger("bankID");
    private final BankIDConfig config;
    private final BankIDClientRegistry registry;

    /**
     * Configuration with default values for BankID.
     */
    public interface BankIDConfig {

        @Attribute(order = 5)
        default EnvironmentType environmentType() {
            return EnvironmentType.TEST;
        }

        /**
         * The client KeyPair Private Encrypted Key
         *
         * @return Encrypted Secret Key
         */
        @Attribute(order = 20, validators = {RequiredValueValidator.class})
        default String clientPemSecretKey() {
            return BankIDEnvironment.testKeyPairSecret;
        }

        /**
         * The encryption key secret.
         *
         * @return the encryption secret
         */
        @Attribute(order = 30, validators = {RequiredValueValidator.class})
        @Password
        default char[] clientPemSecretKeyPassword() {
            return "qwerty123".toCharArray();
        }

        @Attribute(order = 40, validators = {RequiredValueValidator.class})
        default String clientPemPublicKey() {
            return BankIDEnvironment.testKeyPair;
        }

        @Attribute(order = 50)
        default Boolean provisionUser() {
            return Boolean.FALSE;
        }
    }

    /**
     * Constructs a new {@link BankIDNode} with the provided {@link config}.
     *
     * @param config provides the settings for initialising an {@link BankIDNode}.
     * @throws NodeProcessException if there is a problem during construction.
     */
    @Inject
    public BankIDNode(@Assisted BankIDConfig config, BankIDClientRegistry registry) {
        super();
        this.config = config;
        this.registry = registry;
    }

    public enum EnvironmentType {
        TEST("TEST"),
        PROD("PROD");

        private final String environment;

        EnvironmentType(String environment) {
            this.environment = environment;
        }

        public String getEnvironment() {
            return environment;
        }
    }


    @Override
    public Action process(TreeContext treeContext) throws NodeProcessException {
        logger.debug("BankIDAuthenticationNode started");

        try {
            BankIDClient client = registry.get(this.config);

            JsonValue orderRef = treeContext.sharedState.get("orderRef");
            if (orderRef.isNotNull()) {
                try {
                    BankIDClient.HintCode hint = client.collect(orderRef.asString(), treeContext, config.provisionUser());
                    if (hint.success()) {
                        return Action.goTo(TRUE_OUTCOME_ID).build();
                    } else if (hint.pending()) {
                        return send(new TextOutputCallback(TextOutputCallback.INFORMATION, "PENDING")).build();
                    }
                } catch (Exception e) {
                    logger.error("Unable to collect status information information.", e);
                }
                return Action.goTo(FALSE_OUTCOME_ID).build();
            }

            String personalNumber = null;
            JsonValue username = treeContext.sharedState.get(USERNAME);
            if (username.isNotNull()) {
                personalNumber = username.asString();
                if (!personalNumber.matches("^[0-9]{12}$")) {
                    return Action.goTo(FALSE_OUTCOME_ID).build();
                }
            }

            Pair<String, String> pair = client.auth(treeContext.request.clientIp, personalNumber, null);
            treeContext.sharedState.put("orderRef", pair.getFirst());
            return getCallbacksForBankIDInteraction(pair.getSecond(), treeContext);
        } catch (Exception e) {
            logger.error("Unable to authenticate with BankID", e);
            return Action.goTo(FALSE_OUTCOME_ID).build();
        }
    }


    private Action getCallbacksForBankIDInteraction(String autoStartToken, TreeContext context,
                                                    Callback... additionalCallbacks) throws NodeProcessException {
        //ScriptTextOutputCallback registrationCallback = new ScriptTextOutputCallback(script);
        //HiddenValueCallback hiddenValueCallback = new HiddenValueCallback("bankid", "bankid:///?autostarttoken=" + autoStartToken);
        ImmutableList<Callback> callbacks = ImmutableList.<Callback>builder()
                .add(createQRCodeCallback("bankid:///?autostarttoken=" + autoStartToken, 0))
                .add(additionalCallbacks)
                .build();
        return send(callbacks)
                .replaceSharedState(context.sharedState)
                .build();
    }


    private Action collectPersonalNumber(TreeContext context) {
        ResourceBundle bundle = context.request.locales.getBundleInPreferredLocale(BUNDLE, getClass().getClassLoader());
        logger.debug("collecting personal number");
        return send(new NameCallback(bundle.getString("callback.personalNumber"))).build();
    }

    /**
     * There is a hack here to reverse a hack in RESTLoginView.js. Implementing the code properly in RESTLoginView.js so
     * as to remove this hack will take too long at present, and stands in the way of completion of this module's
     * QR code additions. I have opted to simply reverse the hack in this singular case.
     * <p>
     * In the below code returning the ScriptTextOutputCallback, the String used in its construction is
     * defined as follows:
     * <p>
     * createQRDomElementJS
     * Adds the DOM element, in this case a div, in which the QR code will appear.
     * QRCodeGenerationUtilityFunctions.
     * getQRCodeGenerationJavascriptForAuthenticatorAppRegistration(authenticatorAppRegistrationUri)
     * Adds a specific call to the Javascript library code, sending the app registration url as the
     * text to encode as a QR code. This QR code will then appear in the previously defined DOM
     * element (which must have an id of 'qr').
     * hideButtonHack
     * A hack to reverse a hack in RESTLoginView.js. See more detailed comment above.*
     */
    private Callback createQRCodeCallback(String autoStartToken, int callbackIndex) {
        final String callback = "callback_" + callbackIndex;
        return new ScriptTextOutputCallback(
                GenerationUtils.getQRCodeGenerationJavascriptForAuthenticatorAppRegistration(callback,
                        autoStartToken));
    }
}
