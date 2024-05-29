package com.astieoce.divinewhisper.network;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.entity.EntityLevelAccessor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityLevelSyncPacket {
    public static final Identifier ID = new Identifier(DivineWhisper.MOD_ID, "entity_level_sync");
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityLevelSyncPacket.class);

    public static void send(MobEntity entity, int level) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        buf.writeInt(level);

        for (ServerPlayerEntity player : ((ServerWorld) entity.getWorld()).getPlayers()) {
            ServerPlayNetworking.send(player, ID, buf);
            DivineWhisper.LOGGER.debug("Sending Sync Packet, Entity ID: {}, Level: {}", entity.getId(), level);
        }
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            int level = buf.readInt();
            DivineWhisper.LOGGER.debug("Received Sync Packet on Server, Entity ID: {}, Level: {}", entityId, level);
            server.execute(() -> {
                Entity entity = player.getWorld().getEntityById(entityId);
                if (entity instanceof EntityLevelAccessor) {
                    ((EntityLevelAccessor) entity).setEntityLevel(level);
                    DivineWhisper.LOGGER.debug("Applying Sync Packet on Server, Entity ID: {}, Level: {}", entityId, level);
                }
            });
        });
    }
}
