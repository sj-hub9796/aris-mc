package me.ddayo.aris.engine.networking.forge

import me.ddayo.aris.engine.networking.PacketDeclaration
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.forge.ArisForgeNetworking
import me.ddayo.aris.forge.ArisForgeNetworking.GenericC2S
import me.ddayo.aris.forge.ArisForgeNetworking.GenericS2C
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.PacketDistributor

object S2CPacketSenderHandlerImpl {
    /**
     * Send a generic S2C packet to the given player.
     * We extract the packet-ID (a ResourceLocation) and the raw payload bytes
     * from the Builder’s PacketBuffer, then wrap them in our GenericS2C message.
     */
    @JvmStatic
    fun sendS2CPacket(player: LuaServerPlayer, packet: PacketDeclaration.Builder) {
        // builder.build() should return a PacketBuffer containing:
        //   [ResourceLocation id][...payload bytes...]
        val buf = packet.build()
        val key = buf.readResourceLocation()
        val payload = ByteArray(buf.readableBytes()).also { buf.readBytes(it) }

        ArisForgeNetworking.CHANNEL.send(
            PacketDistributor.PLAYER.with { player.player as ServerPlayer },
            GenericS2C(key, payload)
        )
    }
}

object C2SPacketSenderHandlerImpl {
    /**
     * Send a generic C2S packet from client to server.
     * We extract the packet-ID and payload just like on S2C.
     */
    @JvmStatic
    fun sendC2SPacket(packet: PacketDeclaration.Builder) {
        val buf = packet.build()
        val key = buf.readResourceLocation()
        val payload = ByteArray(buf.readableBytes()).also { buf.readBytes(it) }

        ArisForgeNetworking.CHANNEL.sendToServer(
            GenericC2S(key, payload)
        )
    }
}
