package com.astieoce.divinewhisper.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class StarLilyItem extends Item {
    public StarLilyItem(FabricItemSettings settings) {
        super(settings
                .rarity(Rarity.RARE)
        );
    }
}
