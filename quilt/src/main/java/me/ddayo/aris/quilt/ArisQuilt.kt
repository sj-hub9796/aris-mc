package me.ddayo.aris.quilt

import me.ddayo.aris.fabriclike.ArisFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

class ArisQuilt : ModInitializer {
    override fun onInitialize(mod: ModContainer) {
        ArisFabricLike.init()
    }
}
