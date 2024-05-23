package com.astieoce.divinewhisper.camera;

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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CameraSaving {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File RECORDINGS_DIR = new File(MinecraftClient.getInstance().runDirectory, "recordings");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HHmm_ddMMyyyy");

    public static void saveRecording(String filename) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            CameraPath path = CameraControl.cameraPaths.get(client.player.getName().getString());
            if (path != null) {
                if (!RECORDINGS_DIR.exists()) {
                    RECORDINGS_DIR.mkdirs();
                }
                try (FileWriter writer = new FileWriter(new File(RECORDINGS_DIR, filename))) {
                    GSON.toJson(path.getFrames(), writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadRecording(String filename, String playerName) {
        File file = new File(RECORDINGS_DIR, filename);
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<CameraPath.CameraFrame>>() {}.getType();
            List<CameraPath.CameraFrame> frames = GSON.fromJson(reader, listType);
            CameraPath path = new CameraPath();
            path.getFrames().addAll(frames);
            CameraControl.cameraPaths.put(playerName, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<Suggestions> suggestRecordings(SuggestionsBuilder builder) {
        try {
            if (RECORDINGS_DIR.exists()) {
                Files.list(RECORDINGS_DIR.toPath())
                        .filter(Files::isRegularFile)
                        .forEach(path -> builder.suggest(path.getFileName().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.buildFuture();
    }

    public static int listRecordings(FabricClientCommandSource source) {
        try {
            if (RECORDINGS_DIR.exists()) {
                String recordings = Files.list(RECORDINGS_DIR.toPath())
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.joining(", "));

                source.getPlayer().sendMessage(Text.literal("Recordings: " + recordings), false);
            } else {
                source.getPlayer().sendMessage(Text.literal("No recordings found"), false);
            }
        } catch (IOException e) {
            source.getPlayer().sendMessage(Text.literal("Error listing recordings: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return 1;
    }
}
