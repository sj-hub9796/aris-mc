package me.ddayo.aris.lua.engine

import me.ddayo.aris.lua.glue.ClientInGameOnlyGenerated
import party.iroiro.luajava.Lua

class ClientInGameEngine(lua: Lua): ClientBaseEngine(lua) {
    init {
        ClientInGameOnlyGenerated.initLua(lua)
    }
}