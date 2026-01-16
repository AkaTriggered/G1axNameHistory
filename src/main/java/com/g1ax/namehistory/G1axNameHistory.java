package com.g1ax.namehistory;

import com.g1ax.namehistory.commands.NameHistoryCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class G1axNameHistory implements ClientModInitializer {
    public static final String MOD_ID = "g1axnamehistory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("G1axNameHistory initialized!");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
            NameHistoryCommand.register(dispatcher));
    }
}
