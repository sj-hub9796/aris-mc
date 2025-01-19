package me.ddayo.aris.client.engine

import me.ddayo.aris.lua.glue.ClientInGameOnlyGenerated
import party.iroiro.luajava.Lua

class ClientInGameEngine(lua: Lua): ClientMainEngine(lua) {
    companion object {
        const val PROVIDER = "ClientInGameOnlyGenerated"
    }
    init {
        ClientInGameOnlyGenerated.initEngine(this)
    }
}