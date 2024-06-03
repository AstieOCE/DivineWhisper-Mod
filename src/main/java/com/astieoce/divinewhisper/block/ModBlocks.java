package com.astieoce.divinewhisper.block;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.block.alchemy.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<String, Block> GLOWING_GLASS_PANES = new HashMap<>();
    public static final Map<String, Block> ALCHEMICAL_BLOCKS = new HashMap<>();

    static {
        // Register all glowing glass panes
        for (DyeColor color : DyeColor.values()) {
            registerGlowingGlassPane(color.getName() + "_glowing_glass_pane", color);
        }

        // Register alchemical blocks
        registerAlchemyBlock("alchemy_cauldron", new AlchemyCauldronBlock(FabricBlockSettings.create()));
        registerAlchemyBlock("distillation_apparatus", new DistillationApparatusBlock(FabricBlockSettings.create()));
        registerAlchemyBlock("infusion_table", new InfusionTableBlock(FabricBlockSettings.create()));
        registerAlchemyBlock("evaporation_basin", new EvaporationBasinBlock(FabricBlockSettings.create()));
        registerAlchemyBlock("cooling_rack", new CoolingRackBlock(FabricBlockSettings.create()));
        registerAlchemyBlock("filtering_funnel", new FilteringFunnelBlock(FabricBlockSettings.create()));
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

    private static void registerAlchemyBlock(String name, Block block) {
        ALCHEMICAL_BLOCKS.put(name, Registry.register(Registries.BLOCK, new Identifier(DivineWhisper.MOD_ID, name), block));
    }

    public static void registerBlocks() {
        DivineWhisper.LOGGER.info("Registering Mod Blocks for " + DivineWhisper.MOD_ID);
    }
}