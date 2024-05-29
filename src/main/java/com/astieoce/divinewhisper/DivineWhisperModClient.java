package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.camera.CameraCommand;
import com.astieoce.divinewhisper.camera.CameraControl;
import com.astieoce.divinewhisper.renderer.CustomEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;

public class DivineWhisperModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register key bindings
        ModKeyBindings.registerKeyBindings();

        // Register the Event & Callback
        ClientCommandRegistrationCallback.EVENT.register(CameraCommand::register);

        // Register frame recording
        ClientTickEvents.END_CLIENT_TICK.register(client -> CameraControl.recordFrame());

        // Register entity render callback
        WorldRenderEvents.AFTER_ENTITIES.register(this::onRenderWorld);
    }

    private void onRenderWorld(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.world.getEntities().forEach(entity -> {
            int light = client.getEntityRenderDispatcher().getRenderer(entity).getLight(entity, context.tickDelta());
            CustomEntityRenderer.render(entity, context.matrixStack(), context.consumers(), light);
        });
    }
}
