package com.astieoce.divinewhisper.block.alchemy;

import com.astieoce.divinewhisper.util.BrewType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WaterAlchemyCauldronBlock extends CauldronBlock {
    public static final IntProperty IMPURITY = IntProperty.of("impurity", 0, 100);
    public static final EnumProperty<BrewType> BREW_TYPE = EnumProperty.of("brew_type", BrewType.class);

    public WaterAlchemyCauldronBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(IMPURITY, 0).with(BREW_TYPE, BrewType.NORMAL_WATER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IMPURITY, BREW_TYPE);
    }

//    @Override
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        ItemStack itemStack = player.getStackInHand(hand);
//
//        // Handle interaction to possibly update the impurity and brew type
//        // For example purposes, we'll simulate impurity increase
//        if (!world.isClient) {
//            int currentImpurity = state.get(IMPURITY);
//            BrewType currentBrewType = state.get(BREW_TYPE);
//
//            // Simulate impurity increase for demonstration purposes
//            currentImpurity += 1;
//            if (currentImpurity > 60) {
//                currentImpurity = 100;
//                currentBrewType = BrewType.FAILED;
//            }
//
//            world.setBlockState(pos, state.with(IMPURITY, currentImpurity).with(BREW_TYPE, currentBrewType), 3);
//        }
//
//        // Call the parent method for other interactions
//        return super.onUse(state, world, pos, player, hand, hit);
//    }

    @Override
    public boolean isFull(BlockState state) {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int impurity = state.get(IMPURITY);
        BrewType brewType = state.get(BREW_TYPE);

        if (brewType == BrewType.FAILED) {
            if (random.nextFloat() < 0.1f) {
                spawnParticles(world, pos, ParticleTypes.SMOKE, random);
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2f, 0.8f, false);
            }
//        } else if (impurity == 0) {
//            if (random.nextFloat() < 0.3f) {
//                spawnParticles(world, pos, ParticleTypes.HAPPY_VILLAGER, random);
//                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
//            }
            // ANNOYING AS FUCK LMAO.
        } else {
            float chance = (60 - impurity) / 200.0f;
            if (random.nextFloat() < chance) {
                spawnParticles(world, pos, ParticleTypes.SPLASH, random);
            }
        }
    }

    private void spawnParticles(World world, BlockPos pos, ParticleEffect particle, Random random) {
        for (int i = 0; i < 5; i++) { // Number of particles
            double offsetX = random.nextDouble();
            double offsetY = random.nextDouble();
            double offsetZ = random.nextDouble();
            world.addParticle(particle, pos.getX() + offsetX, pos.getY() + 1.0, pos.getZ() + offsetZ, 0.0, 0.0, 0.0);
        }
    }
}
