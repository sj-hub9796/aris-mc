package me.ddayo.aris.client.fabric

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping

object KeyBindingHelperImpl {
    @JvmStatic
    fun registerPlatform(binding: KeyMapping): KeyMapping {
        return KeyBindingHelper.registerKeyBinding(binding)
    }
}