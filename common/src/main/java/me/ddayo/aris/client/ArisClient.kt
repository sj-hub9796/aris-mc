package me.ddayo.aris.client

import me.ddayo.aris.ReferenceMayKeepAlive
import me.ddayo.aris.client.engine.functions.ClientInGameFunction
import me.ddayo.aris.client.engine.ClientEngineManager
import me.ddayo.aris.client.engine.ClientInitEngine
import party.iroiro.luajava.luajit.LuaJit
import java.io.File

object ArisClient {
    @OptIn(ReferenceMayKeepAlive::class)
    fun init() {
        val engine = ClientInitEngine(LuaJit())
        File("robots/client-init").listFiles()?.forEach {
            engine.createTask(it, it.nameWithoutExtension)
        }
        while(engine.tasks.isNotEmpty()) {
            engine.loop()
            engine.removeAllFinished()
        }
    }

    fun clientTick() {
        ClientEngineManager.retrieveGlobalEngine { it.loop() }
    }

    fun clientWorldTick() {
        ClientInGameFunction.tick()
        ClientEngineManager.retrieveInGameEngine { it.loop() }
    }

    fun onClientJoinGame() {
        ClientEngineManager.initInGameEngine()
    }

    fun onClientLeaveGame() {
        ClientEngineManager.disposeInGameEngine()
    }

    fun onClientClose() {
        ClientEngineManager.disposeGlobalEngine()
    }

    fun onClientStart() {
        ClientEngineManager.initGlobalEngine()
    }
}