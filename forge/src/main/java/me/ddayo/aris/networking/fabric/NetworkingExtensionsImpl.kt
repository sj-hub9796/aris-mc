package me.ddayo.aris.networking.fabric

import me.ddayo.aris.forge.ArisForgeNetworking
import net.minecraft.server.level.ServerPlayer

object NetworkingExtensionsImpl {
    @JvmStatic
    fun _sendDataPacket(player: ServerPlayer, of: String, data: Any) {
        ArisForgeNetworking.sendDataPacketForge(player, of, data)
    }

    @JvmStatic
    fun _sendReloadPacket(player: ServerPlayer) {
        ArisForgeNetworking.sendReloadPacketForge(player)
    }
}