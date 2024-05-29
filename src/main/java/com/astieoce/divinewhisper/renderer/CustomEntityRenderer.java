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
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CustomEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEntityRenderer.class);
    private final TextRenderer textRenderer;
    private final EntityRenderer<T> defaultRenderer;
    private static final Map<EntityType<?>, String> ENTITY_NAMES = new HashMap<>();

    static {
        ENTITY_NAMES.put(EntityType.ZOMBIE, "Zombie");
        ENTITY_NAMES.put(EntityType.SKELETON, "Skeleton");
        // Add other entities as needed
    }

    public CustomEntityRenderer(EntityRendererFactory.Context context, EntityRenderer<T> defaultRenderer) {
        super(context);
        this.textRenderer = context.getTextRenderer();
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Render the default entity
        this.defaultRenderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if (entity instanceof EntityLevelAccessor) {
            EntityLevelAccessor accessor = (EntityLevelAccessor) entity;
            int level = accessor.getEntityLevel();

            String entityTypeName = ENTITY_NAMES.getOrDefault(entity.getType(), "Unknown");
            Text name = Text.literal(entityTypeName);
            String levelText = "Level: " + level;

            matrices.push();
            matrices.translate(0.0D, entity.getHeight() + 0.5D, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();

            float nameWidth = textRenderer.getWidth(name);
            float levelTextWidth = textRenderer.getWidth(levelText);

            // Render name
            textRenderer.draw(name.asOrderedText(), -nameWidth / 2.0F, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

            // Render level
            textRenderer.draw(levelText, -levelTextWidth / 2.0F, 10, 0xFFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

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
        // Register other entity types as needed
    }

    public static void addEntityName(EntityType<?> entityType, String name) {
        ENTITY_NAMES.put(entityType, name);
    }

    public static void removeEntityName(EntityType<?> entityType) {
        ENTITY_NAMES.remove(entityType);
    }
}
