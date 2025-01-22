package me.ddayo.aris.engine.client

import me.ddayo.aris.engine.MCBaseEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import party.iroiro.luajava.Lua
import java.io.File

@Environment(EnvType.CLIENT)
open class ClientMainEngine protected constructor (lua: Lua): MCBaseEngine(lua) {
    companion object {
        const val PROVIDER = "LuaClientOnlyGenerated"

        var INSTANCE: ClientMainEngine? = null
            private set

        fun disposeEngine() {
            INSTANCE = null
        }

        fun createEngine(lua: Lua): ClientMainEngine {
            return ClientMainEngine(lua).apply {
                INSTANCE = this

                File("robots/client").listFiles()?.forEach {
                    createTask(it, it.nameWithoutExtension)
                }
            }
        }
    }
    init {
        LuaClientOnlyGenerated.initEngine(this)
    }
}