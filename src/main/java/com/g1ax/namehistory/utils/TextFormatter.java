package com.g1ax.namehistory.utils;

import com.g1ax.namehistory.data.PlayerData;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TextFormatter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");

    public static void sendFormattedHistory(Object source, PlayerData data) {
        sendMessage(source, Text.literal("─────────────────────────────────").formatted(Formatting.DARK_GRAY));
        
        MutableText header = ColorUtils.formatPrefix().append(Text.literal(": Player Info").formatted(Formatting.WHITE));
        sendMessage(source, header);
        sendMessage(source, Text.literal("─────────────────────────────────").formatted(Formatting.DARK_GRAY));
        
        MutableText currentIGN = Text.literal("Current IGN: ").formatted(Formatting.GRAY)
            .append(ColorUtils.applyGradient(data.getCurrentName(), 0xFFD700, 0xFFA500));
        sendMessage(source, currentIGN);
        
        MutableText uuidText = Text.literal("UUID: ").formatted(Formatting.GRAY)
            .append(Text.literal(UUIDUtils.addDashes(data.getUuid())).formatted(Formatting.WHITE)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, data.getUuid()))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy")))));
        sendMessage(source, uuidText);
        
        sendMessage(source, Text.empty());
        sendMessage(source, Text.literal("Name History:").formatted(Formatting.YELLOW));
        
        List<PlayerData.NameEntry> history = data.getNameHistory();
        for (int i = 0; i < history.size(); i++) {
            PlayerData.NameEntry entry = history.get(i);
            MutableText line = Text.literal("  " + (i + 1) + ". ").formatted(Formatting.GRAY)
                .append(Text.literal(entry.getName()).formatted(Formatting.AQUA));
            
            if (i == 0) {
                line.append(Text.literal(" (Current)").formatted(Formatting.GREEN));
            } else if (entry.getChangedAt() != null) {
                String date = DATE_FORMAT.format(new Date(entry.getChangedAt()));
                line.append(Text.literal(" (Changed: " + date + ")").formatted(Formatting.DARK_GRAY));
            } else if (i == history.size() - 1) {
                line.append(Text.literal(" (Original)").formatted(Formatting.DARK_GRAY));
            }
            
            sendMessage(source, line);
        }
        
        sendMessage(source, Text.literal("─────────────────────────────────").formatted(Formatting.DARK_GRAY));
        MutableText footer = Text.literal("Source: " + data.getSource() + " | Cached: ✓").formatted(Formatting.DARK_GRAY);
        sendMessage(source, footer);
    }

    private static void sendMessage(Object source, Text text) {
        if (source instanceof net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource) {
            ((net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource) source).sendFeedback(text);
        }
    }
}
