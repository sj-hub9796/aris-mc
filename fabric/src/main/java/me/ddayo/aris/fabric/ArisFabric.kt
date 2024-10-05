package me.ddayo.aris.fabric

import me.ddayo.fabriclike.ArisFabricLike
import net.fabricmc.api.ModInitializer

class ArisFabric: ModInitializer {
    override fun onInitialize() {
        ArisFabricLike.init()
    }
}