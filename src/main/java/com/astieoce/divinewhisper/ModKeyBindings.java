package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.command.CameraCommand;
import com.astieoce.divinewhisper.screen.DeityScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModKeyBindings {

    public static KeyBinding deityKeyBinding;
    public static KeyBinding recordKeyBinding;

    public static void registerKeyBindings() {
        // Register the deity UI keybinding
        deityKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.divinewhisper.deity_ui", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard
                GLFW.GLFW_KEY_U, // The keycode of the key
                "category.divinewhisper.keybindings" // The translation key of the keybinding's category
        ));

        // Register the recording keybinding
        recordKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.divinewhisper.record", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard
                GLFW.GLFW_KEY_I, // The keycode of the key
                "category.divinewhisper.keybindings" // The translation key of the keybinding's category
        ));

        // Register the tick event to check if the keybindings are pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (deityKeyBinding.wasPressed()) {
                // Open the Deity UI screen
                openDeityUI();
            }
            if (recordKeyBinding.wasPressed()) {
                // Start or stop recording
                toggleRecording();
            }
        });

        // Register the client tick for playback controls
        CameraCommand.registerClientTick();
    }

    private static void openDeityUI() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new DeityScreen());
        DivineWhisper.LOGGER.info("Deity UI key pressed!");
    }

    private static void toggleRecording() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (CameraCommand.isRecording()) {
            CameraCommand.stopRecording();
            String filename = "recording_" + new SimpleDateFormat("HHmm_ddMMyyyy").format(new Date()) + ".json";
            CameraCommand.saveRecording(filename);
            client.player.sendMessage(Text.literal("Stopped recording and saved to " + filename), false);
            DivineWhisper.LOGGER.info("Recording saved to " + filename);
        } else {
            CameraCommand.startRecording();
            client.player.sendMessage(Text.literal("Started recording"), false);
            DivineWhisper.LOGGER.info("Started recording");
        }
    }
}
