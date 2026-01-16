package com.g1ax.namehistory.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ColorUtils {
    public static MutableText applyGradient(String text, int startColor, int endColor) {
        MutableText result = Text.empty();
        int length = text.length();
        
        for (int i = 0; i < length; i++) {
            float ratio = length > 1 ? (float) i / (length - 1) : 0;
            int color = interpolateColor(startColor, endColor, ratio);
            result.append(Text.literal(String.valueOf(text.charAt(i))).styled(style -> style.withColor(color)));
        }
        
        return result;
    }

    public static MutableText formatPrefix() {
        return applyGradient("G1axNameHistory", 0x00FFFF, 0xAA00FF);
    }

    private static int interpolateColor(int start, int end, float ratio) {
        int r1 = (start >> 16) & 0xFF, g1 = (start >> 8) & 0xFF, b1 = start & 0xFF;
        int r2 = (end >> 16) & 0xFF, g2 = (end >> 8) & 0xFF, b2 = end & 0xFF;
        
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);
        
        return (r << 16) | (g << 8) | b;
    }
}
