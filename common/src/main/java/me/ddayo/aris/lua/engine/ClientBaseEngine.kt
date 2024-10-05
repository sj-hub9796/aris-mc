package me.ddayo.aris.lua.engine

import me.ddayo.aris.LuaEngine
import me.ddayo.aris.lua.glue.LuaGenerated

class ClientBaseEngine: LuaEngine() {
    init {
        LuaGenerated.initLua(lua)
    }
}