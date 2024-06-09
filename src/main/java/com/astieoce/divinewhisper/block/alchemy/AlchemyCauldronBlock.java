package com.astieoce.divinewhisper.block.alchemy;

import com.astieoce.divinewhisper.block.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import net.minecraft.text.Text;

public class AlchemyCauldronBlock extends CauldronBlock {

    public AlchemyCauldronBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        // Handle water bucket interaction
        if (itemStack.getItem() == Items.WATER_BUCKET) {
            if (!world.isClient) {
                // Remove the water bucket and give the player an empty bucket
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                // Set the cauldron to be filled with water
                world.setBlockState(pos, BlockRegistry.WATER_ALCHEMY_CAULDRON.getDefaultState());
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
                world.syncWorldEvent(1047, pos, 0);
            }
            return ActionResult.SUCCESS;
        }

        // Open the UI screen if not holding a water bucket
        if (!world.isClient) {
            // Open a dummy UI screen
            player.sendMessage(Text.of("Opening Alchemy Cauldron UI..."), false);
            // TODO: Replace with actual UI screen logic
        }

        return ActionResult.SUCCESS;
    }

    // Prevent filling from dripstone
    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
        // Do nothing to prevent filling from dripstone
    }

    // Prevent filling from precipitation
    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        // Do nothing to prevent filling from precipitation
    }
}
