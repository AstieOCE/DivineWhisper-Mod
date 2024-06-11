//package com.astieoce.divinewhisper.entity;
//
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.SpawnGroup;
//import net.minecraft.entity.ai.goal.LookAroundGoal;
//import net.minecraft.entity.ai.goal.LookAtEntityGoal;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.world.World;
//
//public class BeelzebubsEntity extends LivingEntity {
//
//    public BeelzebubsEntity(EntityType<? extends LivingEntity> type, World world) {
//        super(type, world);
//    }
//
//    @Override
//    protected void initGoals() {
//        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
//        this.goalSelector.add(6, new LookAroundGoal(this));
//    }
//
//    // Implement other necessary methods for your entity
//}
