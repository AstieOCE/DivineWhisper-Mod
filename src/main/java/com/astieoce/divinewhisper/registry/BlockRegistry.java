package com.astieoce.divinewhisper.block;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.block.alchemy.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    public static final Map<String, Block> GLOWING_GLASS_PANES = new HashMap<>();
    public static final Map<String, Block> MOD_BLOCKS = new HashMap<>();

    // Block Declarations
    public static final Block ALCHEMY_CAULDRON = new AlchemyCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON));
    public static final Block WATER_ALCHEMY_CAULDRON = new WaterAlchemyCauldronBlock(FabricBlockSettings.copy(Blocks.WATER_CAULDRON));
    public static final Block ALCHEMY_STATION = new AlchemyStationBlock(FabricBlockSettings.create());
    public static final Block DISTILLATION_APPARATUS = new DistillationApparatusBlock(FabricBlockSettings.create());
    public static final Block EVAPORATION_BASIN = new EvaporationBasinBlock(FabricBlockSettings.create());
    public static final Block COOLING_RACK = new CoolingRackBlock(FabricBlockSettings.create());
    public static final Block FILTERING_FUNNEL = new FilteringFunnelBlock(FabricBlockSettings.create());

    static {
        // Add blocks to the MOD_BLOCKS map
        MOD_BLOCKS.put("alchemy_cauldron", ALCHEMY_CAULDRON);
        MOD_BLOCKS.put("water_alchemy_cauldron", WATER_ALCHEMY_CAULDRON);
        MOD_BLOCKS.put("alchemy_station", ALCHEMY_STATION);
        MOD_BLOCKS.put("distillation_apparatus", DISTILLATION_APPARATUS);
        MOD_BLOCKS.put("evaporation_basin", EVAPORATION_BASIN);
        MOD_BLOCKS.put("cooling_rack", COOLING_RACK);
        MOD_BLOCKS.put("filtering_funnel", FILTERING_FUNNEL);

        // Register all glowing glass panes
        for (DyeColor color : DyeColor.values()) {
            registerGlowingGlassPane(color.getName() + "_glowing_glass_pane", color);
        }
    }

    private static void registerGlowingGlassPane(String name, DyeColor color) {
        Block block = new StainedGlassPaneBlock(color, FabricBlockSettings.create()
                .instrument(Instrument.HAT)
                .strength(0.3f)
                .sounds(BlockSoundGroup.GLASS)
                .luminance(15)
                .nonOpaque());
        GLOWING_GLASS_PANES.put(name, Registry.register(Registries.BLOCK, new Identifier(DivineWhisper.MOD_ID, name), block));
    }

    public static void registerBlocks() {
        DivineWhisper.LOGGER.info("Registering Mod Blocks for " + DivineWhisper.MOD_ID);

        // Dynamic Block Registration
        for (Map.Entry<String, Block> entry : MOD_BLOCKS.entrySet()) {
            Registry.register(Registries.BLOCK, new Identifier(DivineWhisper.MOD_ID, entry.getKey()), entry.getValue());
        }
    }
}
