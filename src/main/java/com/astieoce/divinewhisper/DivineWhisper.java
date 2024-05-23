package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.item.ModItemGroups;
import com.astieoce.divinewhisper.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivineWhisper implements ModInitializer {
	public static final String MOD_ID = "divinewhisper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[DivineWhisper] Initializing.");

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
	}
}
