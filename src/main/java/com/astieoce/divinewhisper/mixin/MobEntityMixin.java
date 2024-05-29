package com.astieoce.divinewhisper.mixin;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements EntityLevelAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobEntityMixin.class);
    private int entityLevel;

    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        DivineWhisper.applyEntityLevel((MobEntity) (Object) this);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        LOGGER.info("Writing EntityLevel to NBT: {}", this.entityLevel);
        nbt.putInt("EntityLevel", this.entityLevel);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("EntityLevel", 3)) { // 3 is the type ID for int
            this.entityLevel = nbt.getInt("EntityLevel");
            LOGGER.info("Reading EntityLevel from NBT: {}", this.entityLevel);
        }
        // Add fallback to update level if it is still 0 after reading NBT
        if (this.entityLevel == 0) {
            LOGGER.info("Entity {} still has level 0 after reading NBT. Updating with a random level.", (Object)this);
            int randomLevel = DivineWhisper.generateRandomLevel();
            this.setEntityLevel(randomLevel);
            LOGGER.info("Entity {} new fallback level: {}", (Object)this, randomLevel);
        }
    }

    @Override
    public int getEntityLevel() {
        return this.entityLevel;
    }

    @Override
    public void setEntityLevel(int level) {
        this.entityLevel = level;
    }
}
