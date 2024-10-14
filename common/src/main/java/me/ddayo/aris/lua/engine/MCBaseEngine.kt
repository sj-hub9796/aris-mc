package me.ddayo.aris.lua.engine

import me.ddayo.aris.LuaEngine
import me.ddayo.aris.lua.glue.LuaGenerated

open class MCBaseEngine: LuaEngine() {
    init {
        LuaGenerated.initLua(lua)
    }
}