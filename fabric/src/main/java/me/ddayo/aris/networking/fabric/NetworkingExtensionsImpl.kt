package me.ddayo.aris.networking.fabric

import me.ddayo.aris.fabriclike.ServerNetworking
import net.minecraft.server.level.ServerPlayer

object NetworkingExtensionsImpl {
    @JvmStatic
    fun _sendDataPacket(player: ServerPlayer, of: String, data: Any) {
        ServerNetworking.sendDataPacketFabricLike(player, of, data)
    }

    @JvmStatic
    fun _sendReloadPacket(player: ServerPlayer) {
        ServerNetworking.sendReloadPacketFabricLike(player)
    }
}