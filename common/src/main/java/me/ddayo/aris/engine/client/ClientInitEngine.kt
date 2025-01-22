package me.ddayo.aris.engine.client

import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.lua.glue.ClientInitGenerated
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import party.iroiro.luajava.Lua
import java.io.File

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

        lua.getGlobal("aris")
        if(lua.isNoneOrNil(-1)) {
            lua.pop(1)
            lua.newTable()
        }
        lua.getField(-1, "init")
        if(lua.isNoneOrNil(-1)) {
            lua.pop(1)
            lua.newTable()
        }
        lua.getGlobal("aris_client_init")
        lua.setField(-2, "client")
        lua.setField(-2, "init")
        lua.setGlobal("aris")

        File("robots/client-init").listFiles()?.forEach {
            createTask(it, it.nameWithoutExtension)
        }
    }
}