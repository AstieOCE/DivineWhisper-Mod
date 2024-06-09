package com.astieoce.divinewhisper.block.entity;

import com.astieoce.divinewhisper.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemyStationBlockEntity extends BlockEntity {
    private final SimpleInventory inventory = new SimpleInventory(25) {
        @Override
        public int getMaxCountPerStack() {
            return 300;
        }
    };

    public AlchemyStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_STATION_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory.stacks);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory.stacks);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }
}
