package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.entity.EntityInit;
import com.astieoce.divinewhisper.item.ModItemGroups;
import com.astieoce.divinewhisper.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivineWhisper implements ModInitializer {
	public static final String MOD_ID = "divinewhisper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
		LOGGER.info("[DivineWhisper] Initializing.");

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		EntityInit.init();
	}
}
