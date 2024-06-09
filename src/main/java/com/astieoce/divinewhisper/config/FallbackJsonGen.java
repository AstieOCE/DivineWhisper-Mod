package com.astieoce.divinewhisper.config;

import com.astieoce.divinewhisper.DivineWhisper;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FallbackJsonGen {

    // Base directories for JSON files
    private static final String BLOCKSTATE_DIR = "src/main/resources/assets/divinewhisper/blockstates/";
    private static final String BLOCK_MODEL_DIR = "src/main/resources/assets/divinewhisper/models/block/";
    private static final String ITEM_MODEL_DIR = "src/main/resources/assets/divinewhisper/models/item/";

    /**
     * This method serves to generate a block's ".json" file in the event that a
     * BLOCK is loaded up without a .json file.
     *
     * @param name NAME of the BLOCK to be generated as a ".json"
     * @param type TYPE of the BLOCK to be generated as a ".json"
     *             (TYPE only accepts "generated", "cube_all", and "custom")
     * @return Number of generated files.
     */
    public static int BLOCK(String name, String type) {
        // Define the paths for the JSON files
        Path blockStatePath = Paths.get(BLOCKSTATE_DIR, name + ".json");
        Path blockModelPath = Paths.get(BLOCK_MODEL_DIR, name + ".json");
        Path itemModelPath = Paths.get(ITEM_MODEL_DIR, name + ".json");

        // Counter to keep track of generated files
        int blockFallbackNum = 0;

        // Generate blockstate JSON file if it doesn't exist
        if (!Files.exists(blockStatePath) && generateBlockStateJson(name, blockStatePath)) {
            blockFallbackNum++;
        }

        // Generate block model JSON file if it doesn't exist
        if (!Files.exists(blockModelPath) && generateBlockModelJson(name, type, blockModelPath)) {
            blockFallbackNum++;
        }

        // Generate item model JSON file if it doesn't exist
        if (!Files.exists(itemModelPath) && generateItemModelJson(name, itemModelPath)) {
            blockFallbackNum++;
        }

        return blockFallbackNum;
    }

    /**
     * This method serves to generate an item's ".json" file in the event that an
     * ITEM is loaded up without a .json file.
     *
     * @param name NAME of the ITEM to be generated as a ".json"
     * @param type TYPE of the ITEM to be generated as a ".json"
     *             (TYPE only accepts "generated", "handheld", and "custom")
     * @return Number of generated files.
     */
    public static int ITEM(String name, String type) {
        // Define the path for the JSON file
        Path itemModelPath = Paths.get(ITEM_MODEL_DIR, name + ".json");

        // Counter to keep track of generated files
        int itemFallbackNum = 0;

        // Generate item model JSON file if it doesn't exist
        if (!Files.exists(itemModelPath) && generateItemModelJson(name, type, itemModelPath)) {
            itemFallbackNum++;
        }

        return itemFallbackNum;
    }

    // Helper methods to generate JSON files
    private static boolean generateBlockStateJson(String name, Path path) {
        JsonObject blockStateJson = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("model", "divinewhisper:block/" + name);
        variants.add("", model);
        blockStateJson.add("variants", variants);
        return writeJsonToFile(blockStateJson, path);
    }

    private static boolean generateBlockModelJson(String name, String type, Path path) {
        JsonObject blockModelJson = new JsonObject();
        if ("cube_all".equals(type)) {
            blockModelJson.addProperty("parent", "block/cube_all");
            JsonObject textures = new JsonObject();
            textures.addProperty("all", "divinewhisper:block/" + name);
            blockModelJson.add("textures", textures);
        } else if ("custom".equals(type)) {
            // Custom type logic can be added here
        } else {
            blockModelJson.addProperty("parent", "block/generated");
        }
        return writeJsonToFile(blockModelJson, path);
    }

    private static boolean generateItemModelJson(String name, String type, Path path) {
        JsonObject itemModelJson = new JsonObject();
        if ("handheld".equals(type)) {
            itemModelJson.addProperty("parent", "item/handheld");
        } else if ("custom".equals(type)) {
            // Custom type logic can be added here
        } else {
            itemModelJson.addProperty("parent", "item/generated");
        }
        itemModelJson.addProperty("textures", "divinewhisper:item/" + name);
        return writeJsonToFile(itemModelJson, path);
    }

    private static boolean generateItemModelJson(String name, Path path) {
        JsonObject itemModelJson = new JsonObject();
        itemModelJson.addProperty("parent", "item/generated");
        itemModelJson.addProperty("textures", "divinewhisper:item/" + name);
        return writeJsonToFile(itemModelJson, path);
    }

    /**
     * Writes a JSON object to a file.
     *
     * @param jsonObject The JSON object to write
     * @param path       The file path to write to
     * @return true if the file was written, false otherwise
     */
    private static boolean writeJsonToFile(JsonObject jsonObject, Path path) {
        try {
            // Ensure the directory exists
            Files.createDirectories(path.getParent());

            try (FileWriter fileWriter = new FileWriter(path.toFile())) {
                fileWriter.write(jsonObject.toString());
                DivineWhisper.LOGGER.info("Generated JSON file: " + path);
                return true;
            }
        } catch (IOException e) {
            DivineWhisper.LOGGER.error("Error writing JSON file: " + path, e);
            return false;
        }
    }
}
