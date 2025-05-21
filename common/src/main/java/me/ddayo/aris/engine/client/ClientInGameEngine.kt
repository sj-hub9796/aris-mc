package me.ddayo.aris.engine.client

import com.mojang.blaze3d.systems.RenderSystem
import me.ddayo.aris.luagen.LuaFunc
import me.ddayo.aris.client.KeyBindingHelper
import me.ddayo.aris.client.gui.HudRenderer
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.ClientInGameOnlyGenerated
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
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

    override val basePath = File("robots/client-game")

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

    fun renderHud(graphics: GuiGraphics, delta: Float) {
        RenderSystem.enableBlend()
        RenderUtil.renderer.loadMatrix(graphics) {
            fixScale(graphics.guiWidth(), graphics.guiHeight(), 1920, 1080) {
                enabledHud.mutableForEach {
                    it.render(this, 0.0, 0.0, delta)
                }
            }
        }
    }

    val packetFunctions = mutableMapOf<ResourceLocation, LuaFunc>()
    val clientStringData = mutableMapOf<String, String>()
    val clientNumberData = mutableMapOf<String, Double>()
    val clientItemStackData = mutableMapOf<String, ItemStack>()
    val tickFunctions = mutableListOf<LuaFunc>()
}