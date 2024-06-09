package com.astieoce.divinewhisper.registry;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.block.BlockRegistry;
import com.astieoce.divinewhisper.item.MemoryEssenceItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ItemRegistry class handles the registration of all items in the DivineWhisper mod.
 * Dynamic basic items (that get auto generated assets and linking).
 * Dynamically register items with specific settings or custom classes.
 */
public class ItemRegistry {
    // List to store all registered mod items
    private static final List<Item> MOD_ITEMS = new ArrayList<>();

    // Map to store items by their names for easy retrieval
    private static final Map<String, Item> ITEM_MAP = new HashMap<>();

    // Specific item declarations for items that need to be referenced directly
    public static final Item MEMORY_ESSENCE = registerItem("memory_essence", new MemoryEssenceItem(new FabricItemSettings()));

    // Define items with their settings type
    // Each entry contains the item name and its type (BASIC or SPECIFIC)
    private static final String[][] AUTO_MOD_ITEMS = {
            {"star_lily", "SPECIFIC"},
            {"void_root", "BASIC"},
            {"mint_moss", "BASIC"},
            {"echora_vine", "BASIC"},
            {"limibloom_flower", "BASIC"},
            {"hangman_root", "BASIC"},
            {"sun_dew", "BASIC"},
            {"shadow_fern", "BASIC"},
            {"magma_char", "BASIC"},
            {"frost_petal", "BASIC"},
            {"aconite", "BASIC"},
            {"purslane", "BASIC"},
            {"alchemy_tome", "BASIC"},
            {"alchemy_recipe", "BASIC"},
            {"cruelsilver_vial", "BASIC"},
            {"ill_mote", "BASIC"},
            {"kind_mote", "BASIC"},
            {"mystica_pearl","BASIC"},
            {"shadow_orchid","BASIC"},
            {"spiramine","BASIC"},
            {"amulum","BASIC"},
            {"ghoulmarr", "BASIC"},
            {"sunflower_oil","BASIC"},
            {"blood_root","BASIC"},
            {"ancient_ice_shard","BASIC"},
            {"mystica_mushroom","BASIC"},
            {"cinder_leaf","BASIC"},
            {"lava_lily","BASIC"},
            {"obsidian_shard","BASIC"},
            {"verdant_moss","BASIC"},
            {"nethershade","BASIC"},
            {"soulflare_dust","BASIC"},

            //{"",""},
    };

    // Static block to register items when the class is loaded
    private static void startDynamicRegistration() {
        // Loop through the ITEMS array and register each item based on its type
        for (String[] item : AUTO_MOD_ITEMS) {
            String name = item[0];
            String type = item[1];
            if ("BASIC".equals(type)) {
                // Register a basic item
                registerItem(name, new Item(new FabricItemSettings()));
            } else if ("SPECIFIC".equals(type)) {
                // Register an item with specific settings or custom class
                registerSpecificItem(name);
            }
        }

        // Register all glowing glass pane items from BlockRegistry.java
        BlockRegistry.GLOWING_GLASS_PANES.forEach((name, block) -> {
            Item item = new BlockItem(block, new FabricItemSettings());
            registerItem(name, item);
        });

        // Register items for all blocks in BlockRegistry
        for (Map.Entry<String, Block> entry : BlockRegistry.MOD_BLOCKS.entrySet()) {
            Item item = new BlockItem(entry.getValue(), new FabricItemSettings());
            registerItem(entry.getKey(), item);
        }
    }

    /**
     * Registers an item with specific settings or custom class.
     * Uses reflection to dynamically load and instantiate the item class.
     * If the item class extends Block, it will not create a new FabricItemSettings instance.
     * If the item class is not found, it logs an error and uses the default FabricItemSettings.
     *
     * @param name The name of the item to register
     */
    private static void registerSpecificItem(String name) {
        try {
            // Construct the full class name assuming it follows the naming convention [TheItemName]Item
            String[] parts = name.split("_");
            StringBuilder classNameBuilder = new StringBuilder("com.astieoce.divinewhisper.item.");
            for (String part : parts) {
                classNameBuilder.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
            }
            classNameBuilder.append("Item");
            String className = classNameBuilder.toString();

            // Load the class dynamically
            Class<?> clazz = Class.forName(className);

            // Check if the item class extends BlockItem or any other Block-related class
            if (BlockItem.class.isAssignableFrom(clazz)) {
                // If the class extends BlockItem, create a new instance with a Block
                Item specificItem = (Item) clazz.getDeclaredConstructor().newInstance();
                // Register the item
                registerItem(name, specificItem);
            } else {
                // Instantiate the class with FabricItemSettings
                Item specificItem = (Item) clazz.getDeclaredConstructor(FabricItemSettings.class).newInstance(new FabricItemSettings());
                // Register the item
                registerItem(name, specificItem);
            }
        } catch (ClassNotFoundException e) {
            // Log the error and register a default item
            DivineWhisper.LOGGER.error("Item class not found: {}", name, e);
            registerItem(name, new Item(new FabricItemSettings()));
        } catch (NoSuchMethodException e) {
            // Log the error and register a default item
            DivineWhisper.LOGGER.error("No suitable constructor found for item: {}", name, e);
            registerItem(name, new Item(new FabricItemSettings()));
        } catch (Exception e) {
            // Log any other error that occurs during reflection
            DivineWhisper.LOGGER.error("Error registering item: {}", name, e);
            registerItem(name, new Item(new FabricItemSettings()));
        }
    }



    /**
     * Registers an item with the given name and settings.
     *
     * @param name The name of the item to register (String)
     * @param item The item instance to register (Item)
     * @return The registered item
     */
    private static Item registerItem(String name, Item item) {
        // Register the item in the Minecraft registry
        Item registeredItem = Registry.register(Registries.ITEM, new Identifier(DivineWhisper.MOD_ID, name), item);
        // Add the item to the list of mod items
        MOD_ITEMS.add(registeredItem);
        // Add the item to the map for easy retrieval
        ITEM_MAP.put(name, registeredItem);
        return registeredItem;
    }

    /**
     * Registers all mod items, counts them and then logs the overall number of items registered.
     * This method is ONLY intended to be called during the mod initialization.
     * YOU HAVE BEEN WARNED :(
     */
    public static void registerModItems() {
        startDynamicRegistration();
        int itemCount = MOD_ITEMS.size();
        DivineWhisper.LOGGER.info("Registering {} Mod Items for: " + DivineWhisper.MOD_ID, itemCount);
    }

    /**
     * Retrieves a list of all the registered mod items.
     *
     * @return A list of all registered mod items (List)
     */
    public static List<Item> getModItems() {
        return MOD_ITEMS;
    }

    /**
     * Retrieves a registered item by its specific name.
     *
     * @param name The name of the item to retrieve (String)
     * @return The item with the specified name, or null if not found
     */
    public static Item getItem(String name) {
        return ITEM_MAP.get(name);
    }
}
