package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.block.ModBlocks;
import com.astieoce.divinewhisper.camera.CameraCommand;
import com.astieoce.divinewhisper.camera.CameraControl;
import com.astieoce.divinewhisper.entity.CustomEntityRenderer;
import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import com.astieoce.divinewhisper.network.EntityLevelSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;

import java.util.Objects;

import static com.astieoce.divinewhisper.DivineWhisper.LOGGER;

public class DivineWhisperModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register key bindings
        ModKeyBindings.registerKeyBindings();

        // Register the Event & Callback
        ClientCommandRegistrationCallback.EVENT.register(CameraCommand::register);

        // Register frame recording
        ClientTickEvents.END_CLIENT_TICK.register(client -> CameraControl.recordFrame());

        // Register Custom Entity Renderer
        CustomEntityRenderer.register();

        setRenderLayers();


        //TODO: This is possibly Obsolete.
        //Register network handler for client-side
//        ClientPlayNetworking.registerGlobalReceiver(EntityLevelSyncPacket.ID, (client, handler, buf, responseSender) -> {
//            int entityId = buf.readInt();
//            int level = buf.readInt();
//            LOGGER.info("Received Sync Packet on Client, Entity ID: {}, Level: {}", entityId, level);
//            client.execute(() -> {
//                Entity entity = client.world.getEntityById(entityId);
//                if (entity instanceof EntityLevelAccessor) {
//                    ((EntityLevelAccessor) entity).setEntityLevel(level);
//                    LOGGER.info("Applying Sync Packet on Client, Entity ID: {}, Level: {}", entityId, level);
//                }
//            });
//        });
    }
    private void setRenderLayers() {
        ModBlocks.GLOWING_GLASS_PANES.values().forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
    }
}
