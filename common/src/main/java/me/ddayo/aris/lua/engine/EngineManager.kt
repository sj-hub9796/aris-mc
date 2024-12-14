package me.ddayo.aris.lua.engine

import com.mojang.blaze3d.systems.RenderSystem
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.luajit.LuaJit

object EngineManager {
    private val lua get() = LuaJit()

    private var globalEngine: ClientBaseEngine? = null
    private var inGameEngine: ClientInGameEngine? = null

    fun initGlobalEngine() {
        RenderSystem.assertOnRenderThread()
        if (globalEngine != null) LogManager.getLogger().warn("Global engine already initialized")
        globalEngine = ClientBaseEngine(lua)
    }

    fun disposeGlobalEngine() {
        RenderSystem.assertOnRenderThread()
        if (globalEngine == null) LogManager.getLogger().warn("Global engine does not exists")
        globalEngine = null
    }

    fun initInGameEngine() {
        RenderSystem.assertOnRenderThread()
        if (inGameEngine != null) LogManager.getLogger().warn("In game engine already initialized")
        inGameEngine = ClientInGameEngine(lua)
    }

    fun disposeInGameEngine() {
        RenderSystem.assertOnRenderThread()
        if (inGameEngine != null) LogManager.getLogger().warn("In game engine does not exists")
        inGameEngine = ClientInGameEngine(lua)
    }

    fun retrieveGlobalEngine(f: (ClientBaseEngine) -> Unit) {
        RenderSystem.assertOnRenderThread()
        f(globalEngine ?: throw IllegalStateException("Global engine is not initialized yet"))
    }

    fun retrieveInGameEngine(f: (ClientInGameEngine) -> Unit) {
        RenderSystem.assertOnRenderThread()
        f(inGameEngine ?: throw IllegalStateException("In game engine is not initialized yet"))
    }
}