package me.ddayo.aris.client.engine

import me.ddayo.aris.engine.MCBaseEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import party.iroiro.luajava.Lua

@Environment(EnvType.CLIENT)
open class ClientMainEngine(lua: Lua): MCBaseEngine(lua) {
    companion object {
        const val PROVIDER = "LuaClientOnlyGenerated"
    }
    init {
        LuaClientOnlyGenerated.initEngine(this)
    }
}