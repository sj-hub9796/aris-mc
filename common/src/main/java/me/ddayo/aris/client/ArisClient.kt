package me.ddayo.aris.client

import me.ddayo.aris.ReferenceMayKeepAlive
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.client.functions.ClientInGameFunction
import me.ddayo.aris.engine.client.ClientInitEngine
import me.ddayo.aris.engine.client.ClientMainEngine
import party.iroiro.luajava.luajit.LuaJit
import java.io.File

object ArisClient {
    @OptIn(ReferenceMayKeepAlive::class)
    fun init() {
        val engine = ClientInitEngine(LuaJit())

        while(engine.tasks.isNotEmpty()) {
            engine.loop()
            engine.removeAllFinished()
        }
    }

    fun clientTick() {
        ClientMainEngine.INSTANCE?.loop()
    }

    fun clientWorldTick() {
        ClientInGameEngine.INSTANCE?.tick()
    }

    fun onClientJoinGame() {
        ClientInGameEngine.createEngine(LuaJit())
    }

    fun onClientLeaveGame() {
        ClientInGameEngine.disposeEngine()
    }

    fun onClientClose() {
        ClientMainEngine.disposeEngine()
    }

    fun onClientStart() {
        ClientMainEngine.createEngine(LuaJit())
    }
}