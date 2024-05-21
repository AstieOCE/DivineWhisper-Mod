package com.astieoce.divinewhisper.item;

import com.astieoce.divinewhisper.DivineWhisper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    // List to store all mod items
    private static final List<Item> MOD_ITEMS = new ArrayList<>();

    // Item declarations
    public static final Item MEMORY_ESSENCE = registerItem("memory_essence", new Item(new FabricItemSettings()));

    // Register a single item and add it to the list
    private static Item registerItem(String name, Item item) {
        Item registeredItem = Registry.register(Registries.ITEM, new Identifier(DivineWhisper.MOD_ID, name), item);
        MOD_ITEMS.add(registeredItem);
        return registeredItem;
    }

    // Add items to the ingredient tab item group
    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        for (Item item : MOD_ITEMS) {
            entries.add(item);
        }
    }

    // Register mod items and item group events
    public static void registerModItems() {
        int itemCount = MOD_ITEMS.size();
        DivineWhisper.LOGGER.info("Registering " + itemCount + " Mod Items for: " + DivineWhisper.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }
}
