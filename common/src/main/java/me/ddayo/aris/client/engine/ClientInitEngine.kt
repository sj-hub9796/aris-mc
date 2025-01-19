package me.ddayo.aris.client.engine

import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.MCBaseEngine
import me.ddayo.aris.engine.client.ClientEngineAddOn
import me.ddayo.aris.lua.glue.ClientInitGenerated
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import party.iroiro.luajava.Lua

@Environment(EnvType.CLIENT)
class ClientInitEngine(lua: Lua): InitEngine(lua) {
    companion object {
        const val PROVIDER = "ClientInitGenerated"
    }
    init {
        ClientInitGenerated.initEngine(this)
        ClientEngineAddOn.clientInitEngineAddOns().forEach {
            it.initLua(this)
        }
    }
}