package com.astieoce.divinewhisper.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class CustomEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    private final TextRenderer textRenderer;
    private final EntityRenderer<T> defaultRenderer;

    public CustomEntityRenderer(EntityRendererFactory.Context context, EntityRenderer<T> defaultRenderer) {
        super(context);
        this.textRenderer = context.getTextRenderer();
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.defaultRenderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if (entity.hasCustomName() || entity.isAlive()) {
            Text name = Text.literal(EntityType.getId(entity.getType()).getPath());
            int level = EntityLevelComponent.getLevel(entity);
            EntityLevelComponent.Tier tier = EntityLevelComponent.getTier(entity);

            String levelTierText = "Lvl: " + level + " - " + tier.name();

            matrices.push();
            matrices.translate(0.0D, entity.getHeight() + 0.5D, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();

            float nameWidth = textRenderer.getWidth(name);
            float levelTierWidth = textRenderer.getWidth(levelTierText);

            textRenderer.draw(name.asOrderedText(), -nameWidth / 2.0F, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
            textRenderer.draw(levelTierText, -levelTierWidth / 2.0F, 10, tier.getColor(), false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

            matrices.pop();
        }
    }

    @Nullable
    @Override
    public Identifier getTexture(T entity) {
        return this.defaultRenderer.getTexture(entity);
    }

    public static void register() {
        EntityRendererRegistry.register(EntityType.ZOMBIE, context -> new CustomEntityRenderer<>(context, new ZombieEntityRenderer(context)));
        EntityRendererRegistry.register(EntityType.SKELETON, context -> new CustomEntityRenderer<>(context, new SkeletonEntityRenderer(context)));
    }
}
