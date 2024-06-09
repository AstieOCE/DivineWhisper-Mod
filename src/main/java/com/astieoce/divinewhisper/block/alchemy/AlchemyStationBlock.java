package com.astieoce.divinewhisper.block.alchemy;

import com.astieoce.divinewhisper.block.entity.AlchemyStationBlockEntity;
import com.astieoce.divinewhisper.screen.AlchemyStationScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemyStationBlock extends Block implements BlockEntityProvider {
    public AlchemyStationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AlchemyStationBlockEntity) {
                NamedScreenHandlerFactory screenHandlerFactory = new SimpleNamedScreenHandlerFactory(
                        (syncId, inventory, playerEntity) -> new AlchemyStationScreenHandler(syncId, inventory, ((AlchemyStationBlockEntity) blockEntity).getInventory()),
                        Text.translatable("container.alchemy_station")
                );
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyStationBlockEntity(pos, state);
    }
}
