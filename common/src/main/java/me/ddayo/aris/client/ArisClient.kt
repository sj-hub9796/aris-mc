package me.ddayo.aris.client

import me.ddayo.aris.client.lua.ClientInGameFunction
import me.ddayo.aris.lua.engine.ClientBaseEngine
import me.ddayo.aris.lua.engine.EngineManager
import net.minecraft.client.Minecraft
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.luajit.LuaJit

object ArisClient {
    fun init() {

    }

    fun clientTick() {
        EngineManager.retrieveGlobalEngine { it.loop() }
    }

    fun clientWorldTick() {
        ClientInGameFunction.tick()
        EngineManager.retrieveInGameEngine { it.loop() }
    }

    fun onClientJoinGame() {
        EngineManager.initInGameEngine()
    }

    fun onClientLeaveGame() {
        EngineManager.disposeInGameEngine()
    }

    fun onClientClose() {
        EngineManager.disposeGlobalEngine()
    }

    fun onClientStart() {
        EngineManager.initGlobalEngine()
    }
}