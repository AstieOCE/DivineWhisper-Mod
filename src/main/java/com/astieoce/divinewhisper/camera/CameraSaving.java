package com.astieoce.divinewhisper.camera;

import com.astieoce.divinewhisper.DivineWhisper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CameraSaving {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File RECORDINGS_DIR = new File(MinecraftClient.getInstance().runDirectory, "recordings");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HHmm_ddMMyyyy");

    public static void saveRecording(String filename, Map<String, Object> settings) {
        if (DivineWhisper.client.player != null) {
            CameraPath path = CameraControl.cameraPaths.get(DivineWhisper.client.player.getName().getString());
            if (path != null) {
                path.setSettings(settings);
                if (!RECORDINGS_DIR.exists()) {
                    RECORDINGS_DIR.mkdirs();
                }
                try (FileWriter writer = new FileWriter(new File(RECORDINGS_DIR, filename))) {
                    GSON.toJson(path, writer);
                } catch (IOException e) {
                    DivineWhisper.LOGGER.error("Failed to save recording", e);
                }
            }
        }
    }

    public static void loadRecording(String filename, String playerName) {
        File file = new File(RECORDINGS_DIR, filename);
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<CameraPath>() {}.getType();
            CameraPath path = GSON.fromJson(reader, type);
            CameraControl.cameraPaths.put(playerName, path);
        } catch (IOException e) {
            DivineWhisper.LOGGER.error("Failed to load recording", e);
        }
    }

    public static CompletableFuture<Suggestions> suggestRecordings(SuggestionsBuilder builder, String input) {
        if (RECORDINGS_DIR.exists()) {
            try (Stream<Path> paths = Files.list(RECORDINGS_DIR.toPath())) {
                List<String> suggestions = paths.filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .collect(Collectors.toList());

                suggestions.forEach(builder::suggest);
            } catch (IOException e) {
                DivineWhisper.LOGGER.error("Failed to suggest recordings", e);
            }
        }
        return builder.buildFuture();
    }

    public static int listRecordings(FabricClientCommandSource source) {
        if (RECORDINGS_DIR.exists()) {
            try (Stream<Path> paths = Files.list(RECORDINGS_DIR.toPath())) {
                String recordings = paths.filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.joining(", "));

                source.getPlayer().sendMessage(Text.literal("Recordings: " + recordings), false);
            } catch (IOException e) {
                source.getPlayer().sendMessage(Text.literal("Error listing recordings: " + e.getMessage()), false);
                DivineWhisper.LOGGER.error("Error listing recordings", e);
            }
        } else {
            source.getPlayer().sendMessage(Text.literal("No recordings found"), false);
        }
        return 1;
    }
}
