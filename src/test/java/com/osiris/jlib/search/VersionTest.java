package com.osiris.jlib.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VersionTest {
    @Test
    void testStandardVersions() {
        String current = "2.2.6";
        String latest = "2.2.5";
        assertFalse(Version.isLatestBigger(current, latest));
        assertTrue(Version.isLatestBiggerOrEqual(current, latest));
        assertTrue(Version.isLatestBiggerOrEqual(latest, latest));
        assertFalse(Version.isLatestBigger(latest, latest));
    }

    @Test
    void testSnapshotWithBuilds() {
        String current = "2.2.4-SNAPSHOT (b116-abcd)";
        String newerSameVersion = "2.2.4-SNAPSHOT (b117-abcd)";
        String olderSameVersion = "2.2.4-SNAPSHOT (b115-xyz)";
        String differentVersion = "2.2.5";

        // Newer build should be considered newer
        assertTrue(Version.isLatestBigger(current, newerSameVersion));
        assertFalse(Version.isLatestBigger(newerSameVersion, current));

        // Older build should not be considered newer
        assertFalse(Version.isLatestBigger(current, olderSameVersion));
        assertTrue(Version.isLatestBigger(olderSameVersion, current));

        // A real version bump should also be detected
        assertTrue(Version.isLatestBigger(current, differentVersion));
    }
}
