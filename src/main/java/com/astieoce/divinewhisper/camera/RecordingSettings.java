package com.astieoce.divinewhisper.camera;

import java.util.HashMap;
import java.util.Map;

public class RecordingSettings {
    private final Map<String, Object> settings = new HashMap<>();

    public RecordingSettings() {
        // Default GENERAL Recording settings
        settings.put("playbackSpeed", 1.0f);
        settings.put("relativePosition", false);
        settings.put("enableGravity", false);
        settings.put("gamemode", "spectator");

        // Default EXTRA Recording Settings
        settings.put("recordInteracts", true);
        settings.put("recordTime", false);
        settings.put("recordInventory", false);
        settings.put("recordGamemode", false);
        settings.put("recordState", false);
        settings.put("recordSelfSkin", false);
        settings.put("recordEntities", false);
        settings.put("recordSounds", false);
    }

    public Object getSetting(String key) {
        return settings.get(key);
    }

    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }

    public Map<String, Object> getAllSettings() {
        return new HashMap<>(settings);
    }
}
