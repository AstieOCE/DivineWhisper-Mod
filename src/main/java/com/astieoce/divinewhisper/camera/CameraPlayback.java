package com.astieoce.divinewhisper.camera;

import com.astieoce.divinewhisper.DivineWhisper;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

import static com.astieoce.divinewhisper.DivineWhisper.client;

public class CameraPlayback {
    private static int speed = 5;
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

            if (!(Boolean) path.getSettings().getOrDefault("relativePosition", false)) {
                Vec3d startPos = path.getFrames().get(0).getPosition();
                client.execute(() -> {
                    if (client.player != null) {
                        client.player.updatePosition(startPos.x, startPos.y, startPos.z);
                        client.player.setYaw(path.getFrames().get(0).getYaw());
                        client.player.setPitch(path.getFrames().get(0).getPitch());
                    }
                });
            }

            new Thread(() -> {
                for (int i = 0; i < path.getFrames().size() - 1 && playingBack; i++) {
                    CameraPath.CameraFrame startFrame = path.getFrames().get(i);
                    CameraPath.CameraFrame endFrame = path.getFrames().get(i + 1);

                    // Calculate the time difference between frames
                    long timeDifference = endFrame.getTimestamp() - startFrame.getTimestamp();
                    int steps = (int) (timeDifference / speed); // Assume 20 FPS, so 50 ms per step

                    for (int j = 0; j < steps && playingBack; j++) { // Steps for smooth transition
                        float t = j / (float) steps;

                        Vec3d interpolatedPos = startFrame.getPosition().lerp(endFrame.getPosition(), t);
                        float interpolatedYaw = lerpAngle(startFrame.getYaw(), endFrame.getYaw(), t);
                        float interpolatedPitch = lerpAngle(startFrame.getPitch(), endFrame.getPitch(), t);

                        try {
                            // Update the player's orientation and key states
                            client.execute(() -> {
                                if (client.player != null) {
                                    client.player.updatePosition(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
                                    client.player.setYaw(interpolatedYaw);
                                    client.player.setPitch(interpolatedPitch);

                                    // Apply key states
                                    Map<String, Boolean> keyStates = startFrame.getKeyStates();
                                    setKeyBindingState(client.options.forwardKey, keyStates.getOrDefault("forward", false));
                                    setKeyBindingState(client.options.backKey, keyStates.getOrDefault("back", false));
                                    setKeyBindingState(client.options.leftKey, keyStates.getOrDefault("left", false));
                                    setKeyBindingState(client.options.rightKey, keyStates.getOrDefault("right", false));
                                    setKeyBindingState(client.options.jumpKey, keyStates.getOrDefault("jump", false));
                                    setKeyBindingState(client.options.sneakKey, keyStates.getOrDefault("sneak", false));

                                    // Apply mouse clicks directly
                                    ClientPlayerEntity player = client.player;
                                    if (startFrame.isLeftClick()) {
                                        if (client.interactionManager != null) {
                                            client.interactionManager.attackEntity(player, player);
                                        }
                                    }
                                    if (startFrame.isRightClick()) {
                                        if (client.interactionManager != null) {
                                            client.interactionManager.interactItem(player, player.getActiveHand());
                                        }
                                    }
                                }
                            });

                            // Sleep for the appropriate duration (interpolated)
                            Thread.sleep(speed); // 5ms per step for smooth playback
                        } catch (InterruptedException e) {
                            DivineWhisper.LOGGER.error("Playback interrupted", e);
                        }
                    }
                }
                stopPlayback(client.player);
            }).start();
        } else {
            source.getPlayer().sendMessage(Text.literal("No recording found in " + filename), false);
        }
    }

    public static void stopPlayback(ClientPlayerEntity player) {
        playingBack = false;
        client.execute(() -> {
            if (player != null) {
                player.setNoGravity(false);
            }
        });
    }

    private static void setKeyBindingState(KeyBinding keyBinding, boolean pressed) {
        if (keyBinding.isPressed() != pressed) {
            keyBinding.setPressed(pressed);
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
