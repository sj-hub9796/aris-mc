package me.ddayo.aris.client.fabric

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping

object KeyBindingHelperImpl {
    @JvmStatic
    fun register(binding: KeyMapping) {
        KeyBindingHelper.registerKeyBinding(binding)
    }
}