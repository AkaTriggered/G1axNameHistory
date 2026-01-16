package com.g1ax.namehistory.api;

import com.g1ax.namehistory.data.PlayerData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CraftyGGApi {
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    public static PlayerData fetchPlayerData(String username) throws Exception {
        String url = "https://api.crafty.gg/api/v2/players/" + username;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(10))
            .header("User-Agent", "G1axNameHistory/1.0")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Player not found");
        }

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        
        if (!root.has("success") || !root.get("success").getAsBoolean()) {
            throw new Exception("Player not found");
        }
        
        JsonObject data = root.getAsJsonObject("data");
        String uuid = data.get("uuid").getAsString();
        String currentName = data.get("username").getAsString();
        
        List<PlayerData.NameEntry> nameHistory = new ArrayList<>();
        JsonArray history = data.getAsJsonArray("usernames");
        
        for (int i = 0; i < history.size(); i++) {
            JsonObject entry = history.get(i).getAsJsonObject();
            String name = entry.get("username").getAsString();
            Long changedAt = entry.has("changed_at") && !entry.get("changed_at").isJsonNull() 
                ? parseTimestamp(entry.get("changed_at").getAsString()) : null;
            nameHistory.add(new PlayerData.NameEntry(name, changedAt));
        }

        return new PlayerData(currentName, uuid, nameHistory, "crafty.gg");
    }
    
    private static Long parseTimestamp(String timestamp) {
        try {
            return java.time.Instant.parse(timestamp).toEpochMilli();
        } catch (Exception e) {
            return null;
        }
    }
}
