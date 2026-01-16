package com.g1ax.namehistory.api;

import com.g1ax.namehistory.cache.DataCacheManager;
import com.g1ax.namehistory.data.PlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.concurrent.CompletableFuture;

public class ApiManager {
    public static CompletableFuture<PlayerData> fetchPlayerDataAsync(String username, Object source) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerData cached = DataCacheManager.getCachedData(username);
            if (cached != null) {
                return cached;
            }

            sendMessage(source, Text.literal("Fetching data...").formatted(Formatting.GRAY));

            try {
                PlayerData data = ApiFallbackHandler.fetchWithFallback(username);
                DataCacheManager.cachePlayerData(username, data);
                return data;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private static void sendMessage(Object source, Text text) {
        if (source instanceof net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource) {
            ((net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource) source).sendFeedback(text);
        }
    }
}
