package me.ddayo.aris.client.forge

import me.ddayo.aris.Aris
import net.minecraft.client.KeyMapping
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

/**
 * Forge implementation of platform-agnostic keybinding registration.
 * Collects all bindings and registers them during the RegisterKeyMappingsEvent.
 */
@Mod.EventBusSubscriber(modid = Aris.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object KeyBindingHelperImpl {
    private val bindings = mutableListOf<KeyMapping>()

    /**
     * Called by common code to queue a KeyMapping for registration.
     */
    @JvmStatic
    fun registerPlatform(binding: KeyMapping): KeyMapping {
        bindings.add(binding)
        return binding
    }

    /**
     * Forge mod event for registering key mappings on the client.
     */
    @SubscribeEvent
    fun onRegisterKeyMappings(event: RegisterKeyMappingsEvent) {
        for (binding in bindings) {
            event.register(binding)
        }
    }
}
