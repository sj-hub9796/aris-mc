package me.ddayo.aris.client

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.client.KeyMapping

object KeyBindingHelper {
    @JvmStatic
    @ExpectPlatform
    fun register(binding: KeyMapping) {
        throw NotImplementedError()
    }
}