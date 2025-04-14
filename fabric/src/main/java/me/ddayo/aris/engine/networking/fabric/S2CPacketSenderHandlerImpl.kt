package me.ddayo.aris.engine.networking.fabric

import me.ddayo.aris.Aris
import me.ddayo.aris.engine.networking.Packet
import me.ddayo.aris.engine.wrapper.LuaPlayerEntity
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

object S2CPacketSenderHandlerImpl {
    @JvmStatic
    fun sendS2CPacket(player: LuaServerPlayer, packet: Packet.Builder) {
        ServerPlayNetworking.send(player.player as ServerPlayer, ResourceLocation(Aris.MOD_ID, "generic_s2c"), packet.build())
    }
}

object C2SPacketSenderHandlerImpl {
    @JvmStatic
    fun sendC2SPacket(packet: Packet.Builder) {
        ClientPlayNetworking.send(ResourceLocation(Aris.MOD_ID, "generic_c2s"), packet.build())
    }
}