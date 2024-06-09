package com.astieoce.divinewhisper.registry;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.entity.BeelzebubsEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class EntityRegistry {

    public static final EntityType<BeelzebubsEntity> BEELZEBUBS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(DivineWhisper.MOD_ID, "beelzebubs"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BeelzebubsEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
                    .build()
    );

    public static void registerEntities() {
        // Register entities here
    }
}
