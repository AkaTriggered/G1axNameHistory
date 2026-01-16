package com.g1ax.namehistory.commands;

import com.g1ax.namehistory.api.ApiManager;
import com.g1ax.namehistory.data.PlayerData;
import com.g1ax.namehistory.utils.TextFormatter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class NameHistoryCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("igns")
            .then(argument("username", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    var client = MinecraftClient.getInstance();
                    var handler = client.getNetworkHandler();
                    java.util.List<String> names = (handler != null)
                        ? handler.getPlayerList().stream()
                            .map(entry -> entry.getProfile().getName())
                            .filter(name -> name != null)
                            .toList()
                        : java.util.List.of();
                    return CommandSource.suggestMatching(names, builder);
                })
                .executes(NameHistoryCommand::execute)));

        dispatcher.register(literal("names")
            .then(argument("username", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    var client = MinecraftClient.getInstance();
                    var handler = client.getNetworkHandler();
                    java.util.List<String> names = (handler != null)
                        ? handler.getPlayerList().stream()
                            .map(entry -> entry.getProfile().getName())
                            .filter(name -> name != null)
                            .toList()
                        : java.util.List.of();
                    return CommandSource.suggestMatching(names, builder);
                })
                .executes(NameHistoryCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        String username = StringArgumentType.getString(context, "username");
        FabricClientCommandSource source = context.getSource();

        ApiManager.fetchPlayerDataAsync(username, source).thenAccept(data -> {
            TextFormatter.sendFormattedHistory(source, data);
        }).exceptionally(e -> {
            source.sendFeedback(Text.literal("⚠ " + e.getMessage()).formatted(Formatting.RED));
            return null;
        });

        return 1;
    }
}
