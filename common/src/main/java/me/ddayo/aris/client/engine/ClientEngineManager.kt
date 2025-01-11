package me.ddayo.aris.client.engine

import com.mojang.blaze3d.systems.RenderSystem
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.luajit.LuaJit

object ClientEngineManager {
    private val lua get() = LuaJit()

    private var globalEngine: ClientMainEngine? = null
    private var inGameEngine: ClientInGameEngine? = null

    fun initGlobalEngine() {
        RenderSystem.assertOnRenderThread()
        if (globalEngine != null) LogManager.getLogger().warn("Global engine already initialized")
        globalEngine = ClientMainEngine(lua)
    }

    fun disposeGlobalEngine() {
        RenderSystem.assertOnRenderThread()
        if (globalEngine == null) LogManager.getLogger().warn("Global engine does not exists")
        globalEngine = null
    }

    fun reloadGlobalEngine() {
        disposeGlobalEngine()
        initGlobalEngine()
    }

    fun initInGameEngine() {
        RenderSystem.assertOnRenderThread()
        if (inGameEngine != null) LogManager.getLogger().warn("In game engine already initialized")
        inGameEngine = ClientInGameEngine(lua)
    }

    fun disposeInGameEngine() {
        RenderSystem.assertOnRenderThread()
        if (inGameEngine != null) LogManager.getLogger().warn("In game engine does not exists")
        inGameEngine = null
    }

    fun reloadInGameEngine() {
        disposeInGameEngine()
        initInGameEngine()
    }

    fun retrieveGlobalEngine(f: (ClientMainEngine) -> Unit) {
        RenderSystem.assertOnRenderThread()
        f(globalEngine ?: throw IllegalStateException("Global engine is not initialized yet"))
    }

    fun retrieveInGameEngine(f: (ClientInGameEngine) -> Unit) {
        RenderSystem.assertOnRenderThread()
        f(inGameEngine ?: throw IllegalStateException("In game engine is not initialized yet"))
    }
}