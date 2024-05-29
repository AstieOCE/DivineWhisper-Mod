package com.astieoce.divinewhisper.renderer;

import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class CustomEntityRenderer {

    public static void render(Entity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity instanceof EntityLevelAccessor && entity instanceof LivingEntity) {
            EntityLevelAccessor accessor = (EntityLevelAccessor) entity;
            int level = accessor.getEntityLevel();
            if (level > 0) {
                String levelText = "Level: " + level;
                renderNameAndLevel((LivingEntity) entity, entity.getDisplayName(), levelText, matrices, vertexConsumers, light);
            }
        }
    }

    private static void renderNameAndLevel(LivingEntity entity, Text name, String levelText, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        float yOffset = entity.getHeight() + 0.5F;

        matrices.push();
        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        matrices.translate(entity.getX() - cameraPos.x, yOffset, entity.getZ() - cameraPos.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MinecraftClient.getInstance().gameRenderer.getCamera().getYaw()));
        matrices.scale(-0.025F, -0.025F, 0.025F);

        // Render the name
        if (name != null && !name.getString().isEmpty()) {
            matrices.push();
            textRenderer.draw(name.asOrderedText(), -textRenderer.getWidth(name) / 2.0F, 0, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
            matrices.pop();
            matrices.translate(0, textRenderer.fontHeight + 2, 0); // Adjust position to be below the name
        }

        // Render the level below the name
        textRenderer.draw(levelText, -textRenderer.getWidth(levelText) / 2.0F, 0, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

        matrices.pop();
    }
}
