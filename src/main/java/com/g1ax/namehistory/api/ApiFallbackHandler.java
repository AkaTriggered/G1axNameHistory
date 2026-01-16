package com.g1ax.namehistory.api;

import com.g1ax.namehistory.data.PlayerData;
import com.g1ax.namehistory.G1axNameHistory;

public class ApiFallbackHandler {
    public static PlayerData fetchWithFallback(String username) throws Exception {
        Exception lastException = null;

        try {
            return CraftyGGApi.fetchPlayerData(username);
        } catch (Exception e) {
            G1axNameHistory.LOGGER.debug("CraftyGG API failed: {}", e.getMessage());
            lastException = e;
        }

        try {
            return MojangApi.fetchPlayerData(username);
        } catch (Exception e) {
            G1axNameHistory.LOGGER.debug("Mojang API failed: {}", e.getMessage());
            lastException = e;
        }

        throw new Exception("Could not fetch player data. Please try again later.");
    }
}
