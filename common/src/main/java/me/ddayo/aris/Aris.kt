package me.ddayo.aris

import me.ddayo.aris.client.lua.ClientInGameFunction
import me.ddayo.aris.lua.engine.ClientBaseEngine
import me.ddayo.aris.lua.engine.ClientInGameEngine
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.luajit.LuaJit

object Aris {
    const val MOD_ID = "aris"
    const val VERSION = "0.1.0"

    fun init() {
        LogManager.getLogger().info("Hello, world")
    }
    fun onServerStart() {
        /*
        LogManager.getLogger().info("onServerStart")
        val engine = ClientInGameEngine(LuaJit())
        val task = engine.createTask("""
            add_tick_hook(function()
            invoke_command("say hello") end)
        """.trimIndent(), "test")
        engine.loop()

        if(task.errorMessage.isNotBlank())
            LogManager.getLogger().error(task.pullError().toString())
         */
    }

}