package com.astieoce.divinewhisper.entity;

import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityLevelComponent {
    private static final Map<Entity, Integer> LEVELS = new HashMap<>();
    private static final Map<Entity, Tier> TIERS = new HashMap<>();

    public static void setLevel(Entity entity, int level) {
        LEVELS.put(entity, level);
    }

    public static int getLevel(Entity entity) {
        return LEVELS.getOrDefault(entity, 1);
    }

    public static void setTier(Entity entity, Tier tier) {
        TIERS.put(entity, tier);
    }

    public static Tier getTier(Entity entity) {
        return TIERS.getOrDefault(entity, Tier.NORMAL);
    }

    public enum Tier {
        NORMAL(0xAAAAAA),
        DANGEROUS(0xFFFF00),
        CHALLENGING(0xFF0000),
        FATAL(0x800080);

        private final int color;

        Tier(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}
