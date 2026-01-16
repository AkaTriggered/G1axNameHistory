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

public class MojangApi {
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    public static PlayerData fetchPlayerData(String username) throws Exception {
        String uuidUrl = "https://api.mojang.com/users/profiles/minecraft/" + username;
        HttpRequest uuidRequest = HttpRequest.newBuilder()
            .uri(URI.create(uuidUrl))
            .timeout(Duration.ofSeconds(10))
            .header("User-Agent", "G1axNameHistory/1.0")
            .GET()
            .build();

        HttpResponse<String> uuidResponse = client.send(uuidRequest, HttpResponse.BodyHandlers.ofString());
        
        if (uuidResponse.statusCode() != 200) {
            throw new Exception("Player not found");
        }

        JsonObject uuidJson = JsonParser.parseString(uuidResponse.body()).getAsJsonObject();
        String uuid = uuidJson.get("id").getAsString();
        String currentName = uuidJson.get("name").getAsString();

        String historyUrl = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        HttpRequest historyRequest = HttpRequest.newBuilder()
            .uri(URI.create(historyUrl))
            .timeout(Duration.ofSeconds(10))
            .header("User-Agent", "G1axNameHistory/1.0")
            .GET()
            .build();

        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
        
        List<PlayerData.NameEntry> nameHistory = new ArrayList<>();
        JsonArray history = JsonParser.parseString(historyResponse.body()).getAsJsonArray();
        
        for (int i = 0; i < history.size(); i++) {
            JsonObject entry = history.get(i).getAsJsonObject();
            String name = entry.get("name").getAsString();
            Long changedAt = entry.has("changedToAt") ? entry.get("changedToAt").getAsLong() : null;
            nameHistory.add(new PlayerData.NameEntry(name, changedAt));
        }

        return new PlayerData(currentName, uuid, nameHistory, "mojang.com");
    }
}
