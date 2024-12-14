package me.ddayo.aris.fabric

import me.ddayo.aris.Aris
import me.ddayo.aris.fabriclike.ArisFabricLike
import me.ddayo.aris.fabriclike.S2CNetworking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class ArisFabric: ModInitializer {
    override fun onInitialize() {
        ArisFabricLike.init()
    }
}