package com.astieoce.divinewhisper.camera;

import com.astieoce.divinewhisper.DivineWhisper;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;

public class CameraPlayback {
    private static boolean playingBack = false;
    private static int currentFrameIndex = 0;
    private static long lastFrameTime = 0;
    private static CameraPath currentPath;

    public static boolean isPlayingBack() {
        return playingBack;
    }

    public static void playback(FabricClientCommandSource source, String filename) {
        String playerName = source.getPlayer().getName().getString();
        CameraSaving.loadRecording(filename, playerName);
        currentPath = CameraControl.cameraPaths.get(playerName);
        if (currentPath != null) {
            source.getPlayer().sendMessage(Text.literal("Playing back recording from " + filename), false);

            // Schedule playback with interpolation
            playingBack = true;
            currentFrameIndex = 0;
            lastFrameTime = System.currentTimeMillis();
            MinecraftClient.getInstance().execute(() -> {
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.setNoGravity(true);
                }
            });
        } else {
            source.getPlayer().sendMessage(Text.literal("No recording found in " + filename), false);
        }
    }

    public static void stopPlayback(net.minecraft.entity.player.PlayerEntity player) {
        playingBack = false;
        currentPath = null;
        MinecraftClient.getInstance().execute(() -> {
            if (player != null) {
                player.setNoGravity(false);
            }
        });
    }

    public static void tick() {
        if (!playingBack || currentPath == null || currentFrameIndex >= currentPath.getFrames().size() - 1) {
            stopPlayback(MinecraftClient.getInstance().player);
            return;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        CameraPath.CameraFrame startFrame = currentPath.getFrames().get(currentFrameIndex);
        CameraPath.CameraFrame endFrame = currentPath.getFrames().get(currentFrameIndex + 1);

        long frameDuration = endFrame.getTimestamp() - startFrame.getTimestamp();
        float progress = Math.min(1.0f, (float) elapsedTime / frameDuration);

        Vec3d interpolatedPos = startFrame.getPosition().lerp(endFrame.getPosition(), progress);
        float interpolatedYaw = lerpAngle(startFrame.getYaw(), endFrame.getYaw(), progress);
        float interpolatedPitch = lerpAngle(startFrame.getPitch(), endFrame.getPitch(), progress);

        MinecraftClient.getInstance().execute(() -> {
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.updatePosition(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
                MinecraftClient.getInstance().player.setYaw(interpolatedYaw);
                MinecraftClient.getInstance().player.setPitch(interpolatedPitch);
            }
        });

        if (progress >= 1.0f) {
            currentFrameIndex++;
            lastFrameTime = currentTime;
        }
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
}
