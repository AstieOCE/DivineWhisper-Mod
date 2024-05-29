package com.astieoce.divinewhisper.network;

import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.server.world.ServerWorld;

public class EntityLevelSyncPacket {
    public static final Identifier ID = new Identifier("divinewhisper", "entity_level_sync");

    public static void send(Entity entity, int level) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        buf.writeInt(level);
        if (entity.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) entity.getWorld();
            Box box = new Box(entity.getBlockPos()).expand(64);
            serverWorld.getPlayers(player -> player.getBoundingBox().intersects(box))
                    .forEach(player -> ServerPlayNetworking.send(player, ID, buf));
        }
    }

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        int level = buf.readInt();
        client.execute(() -> {
            Entity entity = client.world.getEntityById(entityId);
            if (entity instanceof EntityLevelAccessor) {
                ((EntityLevelAccessor) entity).setEntityLevel(level);
            }
        });
    }
}
