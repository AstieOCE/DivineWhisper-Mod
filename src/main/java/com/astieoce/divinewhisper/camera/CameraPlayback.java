package com.astieoce.divinewhisper.camera;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;

public class CameraPlayback {
    private static boolean playingBack = false;

    public static boolean isPlayingBack() {
        return playingBack;
    }

    public static void playback(FabricClientCommandSource source, String filename) {
        String playerName = source.getPlayer().getName().getString();
        CameraSaving.loadRecording(filename, playerName);
        CameraPath path = CameraControl.cameraPaths.get(playerName);
        if (path != null) {
            source.getPlayer().sendMessage(Text.literal("Playing back recording from " + filename), false);

            // Schedule playback with interpolation
            playingBack = true;
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
                stopPlayback(MinecraftClient.getInstance().player);
            }).start();
        } else {
            source.getPlayer().sendMessage(Text.literal("No recording found in " + filename), false);
        }
    }

    public static void stopPlayback(net.minecraft.entity.player.PlayerEntity player) {
        playingBack = false;
        MinecraftClient.getInstance().execute(() -> {
            if (player != null) {
                player.setNoGravity(false);
            }
        });
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
