package com.astieoce.divinewhisper.camera;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class CameraControl {
    public static final Map<String, CameraPath> cameraPaths = new HashMap<>();
    private static boolean recording = false;
    private static long lastRecordedTime = 0;

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
            if (CameraPlayback.isPlayingBack() && client.player != null) {
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
                    CameraPlayback.stopPlayback(client.player);
                    client.player.sendMessage(Text.literal("Playback terminated early! :("), false);
                }
            }
        });
    }
}
