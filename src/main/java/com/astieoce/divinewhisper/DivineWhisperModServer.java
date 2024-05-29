package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.network.EntityLevelSyncPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivineWhisperModServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DivineWhisperServer");

    @Override
    public void onInitializeServer() {
        LOGGER.info("Initializing DivineWhisper Server");
        // Register server-specific events or handlers here
        EntityLevelSyncPacket.register();
    }
}
