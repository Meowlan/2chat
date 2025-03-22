package de.meowlan.twochat;

import de.meowlan.twochat.networking.MessagePacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Twochat implements ModInitializer {
    public static final String MOD_ID = "twochat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing TwoChat mod");

        PayloadTypeRegistry.playS2C().register(MessagePacket.ID, MessagePacket.CODEC);
        TwochatCommands.register();
    }
}