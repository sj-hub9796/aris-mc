package me.ddayo.aris.engine

import me.ddayo.aris.lua.glue.InitGenerated
import party.iroiro.luajava.Lua

open class InitEngine(lua: Lua): MCBaseEngine(lua) {
    companion object {
        const val PROVIDER = "InitGenerated"
    }

    init {
        InitGenerated.initEngine(this)
        EngineAddOn.initEngineAddOns().forEach {
            it.initLua(this)
        }
    }
}