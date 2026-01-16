package com.g1ax.namehistory.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String addDashes(String uuid) {
        if (uuid.contains("-")) return uuid;
        return uuid.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

    public static String removeDashes(String uuid) {
        return uuid.replace("-", "");
    }

    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(addDashes(uuid));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
