package me.ddayo.aris.client

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.client.KeyMapping

object KeyBindingHelper {
    class KeyNotExistsException(name: String): Exception("Key with name $name not exists")

    private val keyBindings = mutableMapOf<String, KeyMapping>()
    fun getKey(name: String) = keyBindings[name] ?: throw KeyNotExistsException(name)
    fun register(binding: KeyMapping) {
        keyBindings[binding.name] = registerPlatform(binding)
    }

    @JvmStatic
    @ExpectPlatform
    fun registerPlatform(binding: KeyMapping): KeyMapping {
        throw NotImplementedError()
    }
}