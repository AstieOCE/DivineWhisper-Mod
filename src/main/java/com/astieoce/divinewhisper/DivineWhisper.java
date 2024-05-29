package com.astieoce.divinewhisper;

import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import com.astieoce.divinewhisper.item.ModItemGroups;
import com.astieoce.divinewhisper.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
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

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		//EntityInit.init();
		//DEPRECIATED.
	}

	public static void applyEntityLevel(MobEntity entity) {
		EntityLevelAccessor accessor = (EntityLevelAccessor) entity;
		int level = accessor.getEntityLevel();
		if (level == 0) {
			LOGGER.info("Entity {} has level 0. Updating with a random level.", entity);
			int randomLevel = generateRandomLevel();
			accessor.setEntityLevel(randomLevel);
			LOGGER.info("Entity {} new level: {}", entity, randomLevel);
		} else {
			LOGGER.info("Entity {} level: {}", entity, level);
		}
	}

	private static int generateRandomLevel() {
		Random random = new Random();
		int minLevel = 1; // Minimum level
		int maxLevel = 80; // Maximum level
		return random.nextInt((maxLevel - minLevel)) + minLevel;
	}
}
