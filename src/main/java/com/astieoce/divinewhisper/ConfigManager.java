package com.astieoce.divinewhisper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.annotation.Obsolete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//TODO: Get this configmanager working.

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE_PATH = "/config/DivineWhisper_Config.json";
    private static boolean debugLoggingEnabled = true;

    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
            Gson gson = new Gson();
            JsonObject config = gson.fromJson(reader, JsonObject.class);
            debugLoggingEnabled = config.get("debug_logging").getAsBoolean();
            LOGGER.info("Debug logging enabled: {}", debugLoggingEnabled);
        } catch (IOException e) {
            LOGGER.error("Failed to load config file", e);
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            Gson gson = new Gson();
            JsonObject config = new JsonObject();
            config.addProperty("debug_logging", debugLoggingEnabled);
            gson.toJson(config, writer);
            LOGGER.info("Config saved successfully");
        } catch (IOException e) {
            LOGGER.error("Failed to save config file", e);
        }
    }

    public static boolean isDebugLoggingEnabled() {
        return debugLoggingEnabled;
    }

    public static void setDebugLoggingEnabled(boolean enabled) {
        debugLoggingEnabled = enabled;
        saveConfig();
    }
}
