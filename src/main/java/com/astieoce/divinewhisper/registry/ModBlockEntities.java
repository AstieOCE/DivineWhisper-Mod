package com.astieoce.divinewhisper.registry;

import com.astieoce.divinewhisper.block.entity.AlchemyStationBlockEntity;
import com.astieoce.divinewhisper.block.BlockRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<AlchemyStationBlockEntity> ALCHEMY_STATION_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        ALCHEMY_STATION_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("divinewhisper", "alchemy_station_block_entity"),
                FabricBlockEntityTypeBuilder.create(AlchemyStationBlockEntity::new, BlockRegistry.ALCHEMY_STATION).build(null)
        );
    }
}
