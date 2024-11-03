package me.ddayo.aris.lua.engine

import me.ddayo.aris.LuaEngine
import me.ddayo.aris.lua.glue.LuaGenerated
import party.iroiro.luajava.Lua

open class MCBaseEngine(lua: Lua): LuaEngine(lua) {
    init {
        LuaGenerated.initLua(lua)
    }
}