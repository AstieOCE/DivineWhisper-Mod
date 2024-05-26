package com.astieoce.divinewhisper.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.mob.HostileEntity;

import java.util.Random;

public class EntityInit {
    private static final Random RANDOM = new Random();

    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof HostileEntity) {
                initializeEntity((HostileEntity) entity);
            }
        });
    }

    private static void initializeEntity(HostileEntity entity) {
        int level = RANDOM.nextInt(80) + 1;
        EntityLevelComponent.Tier tier = getTierFromLevel(level);
        EntityLevelComponent.setLevel(entity, level);
        EntityLevelComponent.setTier(entity, tier);
    }

    private static EntityLevelComponent.Tier getTierFromLevel(int level) {
        if (level <= 20) {
            return EntityLevelComponent.Tier.NORMAL;
        } else if (level <= 40) {
            return EntityLevelComponent.Tier.DANGEROUS;
        } else if (level <= 60) {
            return EntityLevelComponent.Tier.CHALLENGING;
        } else {
            return EntityLevelComponent.Tier.FATAL;
        }
    }
}
