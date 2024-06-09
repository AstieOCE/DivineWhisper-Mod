package com.astieoce.divinewhisper.item;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.registry.ItemRegistry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItemGroups {
    private static final List<ItemGroup> ITEM_GROUPS = new ArrayList<>();

    public static final ItemGroup DIVINEWHISPER_GROUP = registerItemGroup("divinewhisper",
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.divinewhisper"))
                    .icon(() -> new ItemStack(ItemRegistry.MEMORY_ESSENCE))
                    .entries((displayContext, entries) -> {
                        for (Item item : ItemRegistry.getModItems()) {
                            entries.add(new ItemStack(item));
                        }
                    }).build());

    public static final ItemGroup DW_ESSENCE_GROUP = registerItemGroup("dw_essence",
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.dw_essence"))
                    .icon(() -> new ItemStack(Items.DIAMOND))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(Items.DIAMOND_BLOCK));
                    }).build());

    private static ItemGroup registerItemGroup(String name, ItemGroup itemGroup) {
        ItemGroup registeredItemGroup = Registry.register(Registries.ITEM_GROUP, new Identifier(DivineWhisper.MOD_ID, name), itemGroup);
        ITEM_GROUPS.add(registeredItemGroup);
        return registeredItemGroup;
    }

    public static void registerItemGroups() {
        DivineWhisper.LOGGER.info("Registering {} Mod Item Groups for " + DivineWhisper.MOD_ID, ITEM_GROUPS.size());
    }
}
