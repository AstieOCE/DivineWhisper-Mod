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

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements EntityLevelAccessor {

    private int entityLevel;

    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        DivineWhisper.applyEntityLevel((MobEntity) (Object) this);
    }

    @Override
    public int getEntityLevel() {
        return this.entityLevel;
    }

    @Override
    public void setEntityLevel(int level) {
        this.entityLevel = level;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("EntityLevel", this.entityLevel);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("EntityLevel", 3)) { // 3 is the type ID for int
            this.entityLevel = nbt.getInt("EntityLevel");
        }
    }
}
