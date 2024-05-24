package com.astieoce.divinewhisper.camera;

import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

import static com.astieoce.divinewhisper.DivineWhisper.client;

public class CameraControl {
    public static final Map<String, CameraPath> cameraPaths = new HashMap<>();
    private static boolean recording = false;
    private static long lastRecordedTime = 0;
    private static RecordingSettings settings;

    public static void startRecording(RecordingSettings settings) {
        CameraControl.settings = settings;
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
        if (recording && client.player != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordedTime > 50) { // Record every 50ms (20 FPS)
                Vec3d position = client.player.getPos();
                float yaw = client.player.getYaw();
                float pitch = client.player.getPitch();

                // Capture key states
                Map<String, Boolean> keyStates = new HashMap<>();
                keyStates.put("forward", client.options.forwardKey.isPressed());
                keyStates.put("back", client.options.backKey.isPressed());
                keyStates.put("left", client.options.leftKey.isPressed());
                keyStates.put("right", client.options.rightKey.isPressed());
                keyStates.put("jump", client.options.jumpKey.isPressed());
                keyStates.put("sneak", client.options.sneakKey.isPressed());

                // Capture mouse clicks
                boolean leftClick = client.options.attackKey.isPressed();
                boolean rightClick = client.options.useKey.isPressed();

                cameraPaths.get(client.player.getName().getString()).addFrame(position, yaw, pitch, currentTime, keyStates, leftClick, rightClick);
                lastRecordedTime = currentTime;
            }
        }
    }
}
