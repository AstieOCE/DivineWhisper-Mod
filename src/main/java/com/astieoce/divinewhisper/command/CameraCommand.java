package com.astieoce.divinewhisper.command;

import com.astieoce.divinewhisper.camera.CameraPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CameraCommand {
    private static final Map<String, CameraPath> cameraPaths = new HashMap<>();
    private static boolean recording = false;
    private static boolean playingBack = false;
    private static long lastRecordedTime = 0;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File RECORDINGS_DIR = new File(MinecraftClient.getInstance().runDirectory, "recordings");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HHmm_ddMMyyyy");

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("camera")
                .then(literal("record")
                        .executes(context -> startRecordingCommand()))
                .then(literal("stop")
                        .executes(context -> stopRecordingCommand()))
                .then(literal("playback")
                        .then(argument("filename", StringArgumentType.string())
                                .suggests((context, builder) -> suggestRecordings(builder))
                                .executes(context -> playback(context.getSource(), StringArgumentType.getString(context, "filename")))))
                .then(literal("list")
                        .executes(context -> listRecordings(context.getSource()))));
    }

    private static int startRecordingCommand() {
        startRecording();
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Started recording"), false);
        return 1;
    }

    private static int stopRecordingCommand() {
        stopRecording();
        String filename = "recording_" + DATE_FORMAT.format(new Date()) + ".json";
        saveRecording(filename);
        String fullPath = new File(RECORDINGS_DIR, filename).getAbsolutePath();
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Stopped recording and saved to " + fullPath), false);
        return 1;
    }

    public static void startRecording() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            cameraPaths.put(client.player.getName().getString(), new CameraPath());
            recording = true;
        }
    }

    public static void stopRecording() {
        recording = false;
    }

    public static boolean isRecording() {
        return recording;
    }

    public static void saveRecording(String filename) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            CameraPath path = cameraPaths.get(client.player.getName().getString());
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
            cameraPaths.put(playerName, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int playback(FabricClientCommandSource source, String filename) {
        String playerName = source.getPlayer().getName().getString();
        loadRecording(filename, playerName);
        CameraPath path = cameraPaths.get(playerName);
        if (path != null) {
            source.getPlayer().sendMessage(Text.literal("Playing back recording from " + filename), false);

            // Schedule playback with interpolation
            playingBack = true;
            // DISABLE GRAVITY!!! WHOO
            MinecraftClient.getInstance().execute(() -> {
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.setNoGravity(true);
                }
            });
            new Thread(() -> {
                for (int i = 0; i < path.getFrames().size() - 1 && playingBack; i++) {
                    CameraPath.CameraFrame startFrame = path.getFrames().get(i);
                    CameraPath.CameraFrame endFrame = path.getFrames().get(i + 1);

                    // Calculate the time difference between frames
                    long timeDifference = endFrame.getTimestamp() - startFrame.getTimestamp();

                    // Interpolate between frames
                    for (int j = 0; j < 20 && playingBack; j++) { // 20 steps for smooth transition
                        float t = j / 20.0f;

                        Vec3d interpolatedPos = startFrame.getPosition().lerp(endFrame.getPosition(), t);
                        float interpolatedYaw = lerpAngle(startFrame.getYaw(), endFrame.getYaw(), t);
                        float interpolatedPitch = lerpAngle(startFrame.getPitch(), endFrame.getPitch(), t);

                        try {
                            // Update the player's position and orientation
                            MinecraftClient.getInstance().execute(() -> {
                                if (MinecraftClient.getInstance().player != null) {
                                    MinecraftClient.getInstance().player.updatePosition(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
                                    MinecraftClient.getInstance().player.setYaw(interpolatedYaw);
                                    MinecraftClient.getInstance().player.setPitch(interpolatedPitch);
                                }
                            });

                            // Sleep for the appropriate duration (interpolated)
                            Thread.sleep(timeDifference / 20); // Divided by 20 steps
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                playingBack = false;
                MinecraftClient.getInstance().execute(() -> {
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.setNoGravity(false);
                    }
                });
            }).start();
        } else {
            source.getPlayer().sendMessage(Text.literal("No recording found in " + filename), false);
        }
        return 1;
    }

    private static float lerpAngle(float start, float end, float t) {
        float delta = end - start;
        if (delta > 180) {
            delta -= 360;
        } else if (delta < -180) {
            delta += 360;
        }
        return start + t * delta;
    }

    private static CompletableFuture<Suggestions> suggestRecordings(SuggestionsBuilder builder) {
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

    private static int listRecordings(FabricClientCommandSource source) {
        if (RECORDINGS_DIR.exists()) {
            String recordings = Arrays.stream(RECORDINGS_DIR.listFiles())
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.joining(", "));

            source.getPlayer().sendMessage(Text.literal("Recordings: " + recordings), false);
        } else {
            source.getPlayer().sendMessage(Text.literal("No recordings found"), false);
        }
        return 1;
    }

    public static void recordFrame() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (recording && client.player != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordedTime > 50) { // Record every 50ms (20 FPS)
                Vec3d position = client.player.getPos();
                float yaw = client.player.getYaw();
                float pitch = client.player.getPitch();
                cameraPaths.get(client.player.getName().getString()).addFrame(position, yaw, pitch, currentTime);
                lastRecordedTime = currentTime;
            }
        }
    }

    public static void registerClientTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (playingBack && client.player != null) {
                // Disable movement controls
                if (client.options.forwardKey.isPressed() || client.options.backKey.isPressed() ||
                        client.options.leftKey.isPressed() || client.options.rightKey.isPressed() ||
                        client.options.jumpKey.isPressed() || client.options.sneakKey.isPressed()) {
                    client.options.forwardKey.setPressed(false);
                    client.options.backKey.setPressed(false);
                    client.options.leftKey.setPressed(false);
                    client.options.rightKey.setPressed(false);
                    client.options.jumpKey.setPressed(false);
                    client.options.sneakKey.setPressed(false);
                }

                // Check if escape key is pressed to terminate playback
                if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE)) {
                    playingBack = false;
                    client.player.sendMessage(Text.literal("Playback terminated early! :("), false);
                    client.player.setNoGravity(false);
                }
            }
        });
    }
}
