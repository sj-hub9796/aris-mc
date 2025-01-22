package me.ddayo.aris.engine.client

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.KeyBindingHelper
import me.ddayo.aris.client.gui.HudRenderer
import me.ddayo.aris.lua.glue.ClientInGameOnlyGenerated
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.minecraft.client.KeyMapping
import net.minecraft.world.item.ItemStack
import party.iroiro.luajava.Lua
import java.io.File

class ClientInGameEngine protected constructor(lua: Lua) : ClientMainEngine(lua) {
    companion object {
        const val PROVIDER = "ClientInGameOnlyGenerated"
        var INSTANCE: ClientInGameEngine? = null
            private set

        fun disposeEngine() {
            INSTANCE = null
        }

        fun createEngine(lua: Lua): ClientInGameEngine {
            return ClientInGameEngine(lua).apply {
                INSTANCE = this

                File("robots/client-game").listFiles()?.forEach {
                    createTask(it, it.nameWithoutExtension)
                }
            }
        }
    }

    init {
        ClientInGameOnlyGenerated.initEngine(this)
    }

    private val keyBindingHooks = mutableMapOf<KeyMapping, MutableList<LuaFunc>>()

    fun registerKeyHook(name: String, func: LuaFunc) {
        keyBindingHooks.getOrPut(KeyBindingHelper.getKey(name)) { mutableListOf() }
            .add(func)
    }

    fun tick() {
        tickFunctions.mutableForEach { it.call() }
        keyBindingHooks.forEach { (binding, keys) ->
            while (binding.consumeClick())
                keys.mutableForEach { it.call() }
        }
        loop()
    }

    val enabledHud = mutableListOf<HudRenderer>()

    val clientStringData = mutableMapOf<String, String>()
    val clientNumberData = mutableMapOf<String, Double>()
    val clientItemStackData = mutableMapOf<String, ItemStack>()
    val tickFunctions = mutableListOf<LuaFunc>()
}