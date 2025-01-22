package me.ddayo.aris.engine

import me.ddayo.aris.lua.glue.InitGenerated
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.Lua
import java.io.File

open class InitEngine protected constructor(lua: Lua) : MCBaseEngine(lua) {
    companion object {
        const val PROVIDER = "InitGenerated"
        fun create(lua: Lua) = InitEngine(lua).apply {
            File("robots/init").listFiles()?.forEach {
                createTask(it, it.nameWithoutExtension)
            }
        }
    }

    init {
        InitGenerated.initEngine(this)
        EngineAddOn.initEngineAddOns().forEach {
            it.initLua(this)
        }

        lua.getGlobal("aris")
        if (lua.isNoneOrNil(-1)) {
            lua.pop(1)
            lua.newTable()
        }
        lua.getGlobal("aris_init")
        lua.setField(-2, "init")
        lua.setGlobal("aris")
    }
}