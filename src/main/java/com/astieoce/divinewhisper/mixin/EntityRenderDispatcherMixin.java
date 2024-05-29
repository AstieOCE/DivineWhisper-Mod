package com.astieoce.divinewhisper.mixin;

import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof EntityLevelAccessor) {
            EntityLevelAccessor accessor = (EntityLevelAccessor) entity;
            int level = accessor.getEntityLevel();
            if (level > 0) {
                renderLevel(entity, level, matrices, vertexConsumers, light);
            }
        }
    }

    private void renderLevel(Entity entity, int level, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String levelText = "Level: " + level;
        float yOffset = entity.getHeight() + 0.5F;

        matrices.push();
        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        matrices.translate(entity.getX() - cameraPos.x, entity.getY() + yOffset, entity.getZ() - cameraPos.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MinecraftClient.getInstance().gameRenderer.getCamera().getYaw()));
        matrices.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        textRenderer.draw(levelText, -textRenderer.getWidth(levelText) / 2.0F, 0, 0xFFFFFF, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

        matrices.pop();
    }
}
