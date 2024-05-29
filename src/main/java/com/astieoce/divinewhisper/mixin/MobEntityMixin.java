package com.astieoce.divinewhisper.mixin;

import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements EntityLevelAccessor {
    @Unique
    private static final TrackedData<Integer> ENTITY_LEVEL = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    private void onInitDataTracker(CallbackInfo ci) {
        ((MobEntity) (Object) this).getDataTracker().startTracking(ENTITY_LEVEL, 0);
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (!world.isClient()) {
            int level = world.getRandom().nextInt(80) + 1; // Random level between 1 and 80
            ((MobEntity) (Object) this).getDataTracker().set(ENTITY_LEVEL, level);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("EntityLevel", ((MobEntity) (Object) this).getDataTracker().get(ENTITY_LEVEL));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("EntityLevel", 3)) {
            ((MobEntity) (Object) this).getDataTracker().set(ENTITY_LEVEL, nbt.getInt("EntityLevel"));
        }
    }

    @Override
    public int getEntityLevel() {
        return ((MobEntity) (Object) this).getDataTracker().get(ENTITY_LEVEL);
    }

    @Override
    public void setEntityLevel(int level) {
        ((MobEntity) (Object) this).getDataTracker().set(ENTITY_LEVEL, level);
    }
}
