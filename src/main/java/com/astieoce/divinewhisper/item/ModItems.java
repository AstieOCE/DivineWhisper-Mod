package com.astieoce.divinewhisper.item;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    // List to store all mod items
    private static final List<Item> MOD_ITEMS = new ArrayList<>();

    // Item declarations - This feels like a shit way to do it, but its what tutorials have done. so...
    public static final Item MEMORY_ESSENCE = registerItem("memory_essence", new MemoryEssenceItem(new FabricItemSettings()));
    public static final Item STAR_LILY = registerItem("star_lily", new Item(new FabricItemSettings()));
    public static final Item VOID_ROOT = registerItem("void_root", new Item(new FabricItemSettings()));
    public static final Item MINT_MOSS = registerItem("mint_moss", new Item(new FabricItemSettings()));
    public static final Item ECHORA_VINE = registerItem("echora_vine", new Item(new FabricItemSettings()));
    public static final Item LIMIBLOOM_FLOWER = registerItem("limibloom_flower", new Item(new FabricItemSettings()));
    public static final Item HANGMAN_ROOT = registerItem("hangman_root", new Item(new FabricItemSettings()));
    public static final Item SUN_DEW = registerItem("sun_dew", new Item(new FabricItemSettings()));
    public static final Item SHADOW_FERN = registerItem("shadow_fern", new Item(new FabricItemSettings()));
    public static final Item MAGMA_CHAR = registerItem("magma_char", new Item(new FabricItemSettings()));
    public static final Item FROST_PETAL = registerItem("frost_petal", new Item(new FabricItemSettings()));
    public static final Item ALCHEMY_TOME = registerItem("alchemy_tome", new Item(new FabricItemSettings()));
    public static final Item ALCHEMY_RECIPE = registerItem("alchemy_recipe", new Item(new FabricItemSettings()));
    public static final Item CRUELSILVER_VIAL = registerItem("cruelsilver_vial", new Item(new FabricItemSettings()));
    //TODO: Finish the above items, as they are currently just placeholders for now. Therefore, do NOTHING.


    static {
        // Register all glowing glass pane items
        ModBlocks.GLOWING_GLASS_PANES.forEach((name, block) -> {
            Item item = new BlockItem(block, new FabricItemSettings());
            MOD_ITEMS.add(Registry.register(Registries.ITEM, new Identifier(DivineWhisper.MOD_ID, name), item));
        });
        // Register all alchemical blocks as items.
        ModBlocks.ALCHEMICAL_BLOCKS.forEach((name, block) -> {
            Item item = new BlockItem(block, new FabricItemSettings());
            MOD_ITEMS.add(Registry.register(Registries.ITEM, new Identifier(DivineWhisper.MOD_ID, name), item));
        });
    }

    // Register a single item and add it to the list
    // Probably should remove this at some stage, as this feels yuck and shit LMAO.
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
        DivineWhisper.LOGGER.info("Registering {} Mod Items for: " + DivineWhisper.MOD_ID, itemCount);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }

    // Expose MOD_ITEMS to other classes
    public static List<Item> getModItems() {
        return MOD_ITEMS;
    }
}
