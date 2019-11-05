package org.forgerock.auth.node.bankid;

import com.google.common.collect.ImmutableMap;
import org.forgerock.openam.auth.node.api.AbstractNodeAmPlugin;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.plugins.PluginException;

import java.util.Map;

import static java.util.Arrays.asList;

public class BankIDNodePlugin extends AbstractNodeAmPlugin {
    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    @Override
    public void upgrade(String fromVersion) throws PluginException {
        super.upgrade(fromVersion);
    }

    @Override
    protected Map<String, Iterable<? extends Class<? extends Node>>> getNodesByVersion() {
        return ImmutableMap.of("1.0.0", asList(BankIDNode.class));
    }
}
