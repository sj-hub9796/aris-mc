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

        File("robots/client-init").listFiles()?.forEach {
            createTask(it, it.nameWithoutExtension)
        }
    }
}