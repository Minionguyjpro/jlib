package com.osiris.jlib.search;

import java.util.Objects;

public class Version {

    /**
     * Compares the current version with the latest
     * version and returns true if the latest version is
     * bigger than the current version. <br>
     * Note that empty strings are handled as 0. <br>
     * If the version is malformed like 1..0 or 1.0...1 the additional dots get removed.
     */
    public static boolean isLatestBigger(String currentVersion, String latestVersion) {
        String[] arrCurrent = cleanAndSplitByDots(currentVersion);
        String[] arrLatest = cleanAndSplitByDots(latestVersion);

        int minLength = Math.min(arrCurrent.length, arrLatest.length);
        for (int i = 0; i < minLength; i++) {
            if (!arrCurrent[i].equals(arrLatest[i])) {
                return isFirstBigger(arrLatest[i], arrCurrent[i]);
            }
        }

        // If numeric parts are equal, compare lengths (ignoring build metadata)
        if (arrCurrent.length != arrLatest.length) {
            return arrLatest.length > arrCurrent.length;
        }

        // If numeric parts and lengths are equal, consider build metadata
        return currentVersion.compareTo(latestVersion) < 0;
    }

    /**
     * Compares the current version with the latest
     * version and returns true if the latest version is
     * bigger or equal to the current version. <br>
     * Note that empty strings are handled as 0. <br>
     * If the version is malformed like 1..0 or 1.0...1 the additional dots get removed.
     */
    public static boolean isLatestBiggerOrEqual(String currentVersion, String latestVersion) {
        String[] arrCurrent = cleanAndSplitByDots(currentVersion);
        String[] arrLatest = cleanAndSplitByDots(latestVersion);

        if (arrLatest.length == arrCurrent.length) {
            String latest, current;
            for (int i = 0; i < arrLatest.length; i++) {
                latest = arrLatest[i];
                current = arrCurrent[i];
                if (latest.equals(current)) continue;
                else return isFirstBiggerOrEqual(latest, current);
            }
            return true; // All are the same
        } else return arrLatest.length >= arrCurrent.length;
    }

    public static String[] cleanAndSplitByDots(String version) {
        Objects.requireNonNull(version);
        version = version.trim();

        // Extract numeric build number from something like (b116-abcd)
        int idx = version.indexOf("(b");
        if (idx != -1) {
            int endIdx = version.indexOf(")", idx);
            if (endIdx != -1) {
                String build = version.substring(idx + 2, endIdx).split("-")[0]; // get only numeric part
                version = version.substring(0, idx).trim() + "." + build; // append build number as new segment
            }
        }
        // Strip out non-numeric and non-dot characters
        version = version.replaceAll("[^0-9.]", "");
        // Replace multiple dots with a single one
        while (version.contains("..")) version = version.replace("..", ".");
        if (version.isEmpty()) return new String[]{"0"};
        return version.split("\\.");
    }

    public static boolean isFirstBigger(String num1, String num2) {
        if (num1.length() > num2.length()) {
            return true;
        } else if (num1.length() < num2.length()) {
            return false;
        } else {
            // If the lengths are equal, compare character by character.
            for (int i = 0; i < num1.length(); i++) {
                char digit1 = num1.charAt(i);
                char digit2 = num2.charAt(i);
                if (digit1 > digit2) {
                    return true;
                } else if (digit1 < digit2) {
                    return false;
                }
            }
        }
        // If all characters are the same, consider them equal.
        return false;
    }

    public static boolean isFirstBiggerOrEqual(String num1, String num2) {
        if (num1.length() >= num2.length()) {
            return true;
        } else if (num1.length() < num2.length()) {
            return false;
        } else {
            // If the lengths are equal, compare character by character.
            for (int i = 0; i < num1.length(); i++) {
                char digit1 = num1.charAt(i);
                char digit2 = num2.charAt(i);
                if (digit1 > digit2) {
                    return true;
                } else if (digit1 < digit2) {
                    return false;
                }
            }
        }
        // If all characters are the same, consider them equal.
        return true;
    }

}
