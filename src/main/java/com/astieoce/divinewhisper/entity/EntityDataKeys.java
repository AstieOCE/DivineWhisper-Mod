package com.astieoce.divinewhisper.entity;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

public class EntityDataKeys {
    public static final TrackedData<Integer> LEVEL = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> TIER = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final TrackedDataHandler<Integer> TIER_HANDLER = new TrackedDataHandler<>() {
        @Override
        public void write(PacketByteBuf buf, Integer value) {
            buf.writeInt(value);
        }

        @Override
        public Integer read(PacketByteBuf buf) {
            return buf.readInt();
        }

        @Override
        public Integer copy(Integer value) {
            return value;
        }
    };

    static {
        TrackedDataHandlerRegistry.register(TIER_HANDLER);
    }
}
