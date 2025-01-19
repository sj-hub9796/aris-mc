package me.ddayo.aris.client

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.client.KeyMapping

object KeyBindingHelper {
    class KeyNotExistsException(name: String): Exception("Key with name $name not exists")

    private val keyBindings = mutableMapOf<String, Pair<KeyMapping, MutableList<() -> Unit>>>()
    fun register(binding: KeyMapping) {
        keyBindings[binding.name] = (registerPlatform(binding) to mutableListOf())
    }

    fun registerAction(key: String, action: () -> Unit) {
        keyBindings[key]?.second?.add(action) ?: throw KeyNotExistsException(key)
    }

    fun clearBindingActions() = keyBindings.values.forEach { it.second.clear() }

    @JvmStatic
    @ExpectPlatform
    fun registerPlatform(binding: KeyMapping): KeyMapping {
        throw NotImplementedError()
    }

    fun tickKeyBindings() {
        keyBindings.forEach { name, (binding, action) ->
            while(binding.consumeClick())
                action.forEach { it() }
        }
    }
}