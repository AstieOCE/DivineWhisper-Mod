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
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO(Basic Settings): Enable the ability to add "Settings" the saved json files.
// enableGravity: This prevents the player character from falling if they're in the air or jumping.
// (DEFAULT: FALSE)
// recordTime: This records the current world's TIME and will replicate it when playback occurs.
// (DEFAULT: FALSE)
// relativePosition: This makes XYZ positioning & direction the player is facing RELATIVE to CURRENT PLAYER position.
// (DEFAULT: FALSE)

// TODO(Extra Settings) - Extra settings that MUST return whatever was PREVIOUSLY there, BACK + remove anything extra.
// playbackSpeed: Changing this value will change the playback recording's speed.
// (DEFAULT: 1)
// recordInventory: This records the player's CURRENT inventory & which slot they're on. Including if they use an item.
// (DEFAULT: FALSE)
// recordGamemode: This records PLAYER gamemode. I HIGHLY recommend against going into CREATIVE mode. (but allow it)
// (DEFAULT: FALSE)
// recordState: This will record PLAYER HEALTH, HUNGER and EFFECTS on PLAYER. WILL return to previous values.
// (DEFAULT: FALSE)
// recordEntities: This records entities within a 6 chunk radius of the player AND their own movements + yaw etc.
// (DEFAULT: FALSE)

// TODO(Error Checking): Ensure there is obsolete json file correction OR ignoring the errors and stating it.

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
        String input = builder.getRemainingLowerCase();

        if (RECORDINGS_DIR.exists()) {
            try (Stream<Path> paths = Files.list(RECORDINGS_DIR.toPath())) {
                List<String> suggestions = paths.filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .sorted(Comparator.comparingInt(s -> getLevenshteinDistance(s.toLowerCase(), input)))
                        .collect(Collectors.toList());

                suggestions.forEach(builder::suggest);
            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        } else {
            source.getPlayer().sendMessage(Text.literal("No recordings found"), false);
        }
        return 1;
    }

    private static int getLevenshteinDistance(String a, String b) {
        int[][] costs = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    costs[i][j] = j;
                } else if (j == 0) {
                    costs[i][j] = i;
                } else {
                    int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                    costs[i][j] = Math.min(Math.min(costs[i - 1][j] + 1, costs[i][j - 1] + 1), costs[i - 1][j - 1] + cost);
                }
            }
        }
        return costs[a.length()][b.length()];
    }
}
