package me.ddayo.aris.lua.engine

import me.ddayo.aris.LuaEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.lua.glue.LuaGenerated
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import party.iroiro.luajava.Lua

@Environment(EnvType.CLIENT)
class ClientBaseEngine(lua: Lua): MCBaseEngine(lua) {
    init {
        LuaClientOnlyGenerated.initLua(lua)
    }
}