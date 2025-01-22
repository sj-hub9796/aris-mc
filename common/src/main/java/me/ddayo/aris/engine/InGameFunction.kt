package me.ddayo.aris.engine

import me.ddayo.aris.Aris
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.luagen.RetrieveEngine


@LuaProvider(InGameEngine.PROVIDER, library = "aris")
object InGameFunction {
    @LuaFunction("add_on_use_item")
    fun onUseItemHook(@RetrieveEngine engine: InGameEngine, item: String, func: LuaFunc) {
        engine.itemUseHook.getOrPut(item) { mutableListOf() }.add(func)
    }

    @LuaFunction("clear_on_use_item")
    fun clearOnUseItem(@RetrieveEngine engine: InGameEngine, item: String) {
        engine.itemUseHook[item] = mutableListOf()
    }

    @LuaFunction("dispatch_command")
    fun dispatchCommand(command: String) {
        val server = Aris.server
        val dispatcher = server.commands.dispatcher
        val results = dispatcher.parse(command, server.createCommandSourceStack())
        server.commands.performCommand(results, command)
    }
}