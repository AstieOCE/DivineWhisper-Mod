package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.block.BlockRegistry;
import com.astieoce.divinewhisper.camera.CameraCommand;
import com.astieoce.divinewhisper.camera.CameraControl;
import com.astieoce.divinewhisper.entity.CustomEntityRenderer;
import com.astieoce.divinewhisper.registry.EntityRegistry;
import com.astieoce.divinewhisper.registry.ModScreenHandlers;
import com.astieoce.divinewhisper.util.BrewColorHandling;
import com.astieoce.divinewhisper.util.BrewType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

import com.astieoce.divinewhisper.block.alchemy.WaterAlchemyCauldronBlock;
import com.astieoce.divinewhisper.screen.*;

public class DivineWhisperModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register key bindings
        ModKeyBindings.registerKeyBindings();

        // Register SINGULAR Screenhandler.
        // TODO: Make this not just register ONE at a time lmao.
        ScreenRegistry.register(ModScreenHandlers.ALCHEMY_STATION_SCREEN_HANDLER, AlchemyStationScreen::new);
        EntityRendererRegistry.register(EntityRegistry.BEELZEBUBS, BeelzebubsEntityRenderer::new);
        // Register the Event & Callback
        ClientCommandRegistrationCallback.EVENT.register(CameraCommand::register);

        // Register frame recording
        ClientTickEvents.END_CLIENT_TICK.register(client -> CameraControl.recordFrame());

        // Register Custom Entity Renderer
        CustomEntityRenderer.register();

        setRenderLayers();

        registerBlockColors();
    }

    private void registerBlockColors() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                int impurity = state.get(WaterAlchemyCauldronBlock.IMPURITY);
                BrewType brewType = state.get(WaterAlchemyCauldronBlock.BREW_TYPE);

                if (impurity > 60) {
                    return BrewType.FAILED.getColor(); // Failed brew color
                }

                int baseColor = brewType.getColor();
                return BrewColorHandling.applyImpurityToColor(baseColor, impurity);
            }
            return -1; // No color tint for other tint indices
        }, BlockRegistry.WATER_ALCHEMY_CAULDRON);
    }

        // NOTE: THE "setRenderLayers()" IS BELOW THIS OLD CODE!!!

        //TODO: This is possibly Obsolete Code. Will need to test/debug this and the other network packet shit.
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

    private void setRenderLayers() {
        BlockRegistry.GLOWING_GLASS_PANES.values().forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
    }
}
