package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.block.BlockRegistry;
import com.astieoce.divinewhisper.camera.CameraPlayback;
import com.astieoce.divinewhisper.item.ModItemGroups;
import com.astieoce.divinewhisper.registry.EntityRegistry;
import com.astieoce.divinewhisper.registry.ItemRegistry;
import com.astieoce.divinewhisper.registry.ModBlockEntities;
import com.astieoce.divinewhisper.registry.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class DivineWhisper implements ModInitializer {
	public static final String MOD_ID = "divinewhisper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
		LOGGER.info("[DivineWhisper] Initializing.");
		LOGGER.debug("[DivineWhisper] Mod is loading in Debugging Environment. Hell0 thar3!");
		//TODO: English language file! I haven't done the normal items yet! oosp!
		//TODO: Setup the specific debug logger with debug config shit, OR just use the .debug thing.

		// Doesn't work currently.
	//	ConfigManager.loadConfig();

		// Working.
		ItemRegistry.registerModItems();
		ModBlockEntities.registerBlockEntities();
		BlockRegistry.registerBlocks();
		EntityRegistry.registerEntities();
		ModScreenHandlers.registerAllScreenHandlers();

		//TODO: Remove ModItemGroups, move to registry package, rename like ItemRegistry.java class.
		ModItemGroups.registerItemGroups();


		// Register the tick event to call the CameraPlayback.tick method
		ClientTickEvents.END_CLIENT_TICK.register(client -> CameraPlayback.tick());

	}

	public static int generateRandomLevel() {
		Random random = new Random();
		int minLevel = 1; // Minimum level
		int maxLevel = 80; // Maximum level
		return random.nextInt((maxLevel - minLevel + 1)) + minLevel;
	}
}
