package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.camera.CameraCommand;
import com.astieoce.divinewhisper.camera.CameraControl;
import com.astieoce.divinewhisper.entity.CustomEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


public class DivineWhisperModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register key bindings
        ModKeyBindings.registerKeyBindings();

        // Register the Event & Callback
        ClientCommandRegistrationCallback.EVENT.register(CameraCommand::register);

        // Register frame recording
        ClientTickEvents.END_CLIENT_TICK.register(client -> CameraControl.recordFrame());

        // Register Custom Entity Renderer... It's shit but it works lmao ;_;
        CustomEntityRenderer.register();
    }
}
