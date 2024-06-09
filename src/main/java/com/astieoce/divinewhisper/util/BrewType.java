package com.astieoce.divinewhisper.util;

import net.minecraft.util.StringIdentifiable;

public enum BrewType implements StringIdentifiable {
    HEALTH("health_brew", 0xFF0000, true),
    REGENERATION("regeneration_brew", 0xF4A54D, true),
    NORMAL_WATER("normal_water", 0x3F76E4, false),
    INITIAL_BREW("initial_brew", 0xFFC1BCE8, false),
    FAILED("failed_brew", 0xFF262433, false);

    private final String name;
    private final int color;
    private final boolean impurityMatters;

    BrewType(String name, int color, boolean impurityMatters) {
        this.name = name;
        this.color = color;
        this.impurityMatters = impurityMatters;
    }

    @Override
    public String asString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean doesImpurityMatter() {
        return impurityMatters;
    }
}
