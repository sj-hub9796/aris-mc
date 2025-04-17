package me.ddayo.aris.engine

import me.ddayo.aris.LuaEngine
import me.ddayo.aris.lua.glue.LuaGenerated
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.Lua
import java.io.File

open class MCBaseEngine(lua: Lua) : LuaEngine(lua, errorMessageHandler = { LogManager.getLogger().error(it) }) {
    init {
        LuaGenerated.initEngine(this)

        lua.push { lua ->
            val task = lua.toString(-1)!!
            lua.pop(1)
            lua.push(isProcessFinished(task))
            1
        }
        lua.setGlobal("__aris_internal_is_processed")

        lua.load("""
            function depends_on(task)
                local cnt = 0
                while not __aris_internal_is_processed(task) do
                    task_yield()
                    cnt = cnt + 1
                    if cnt == 100000 then error("task dependency too deep or recursive dependency detected!!") end
                end
            end
        """.trimIndent())
        lua.pCall(0, 0)
    }

    private val processedTasks = mutableSetOf<String>()

    fun isProcessFinished(check: String) = processedTasks.contains(check) && this.tasks.none { it.name == check }

    fun createTask(file: File, _name: String? = null) {
        if (!file.exists()) return
        val name = _name ?: file.relativeTo(basePath).absolutePath
        createTask(file.readText(), name)
    }

    open val basePath: File = File("robots")

    override fun createTask(code: String, name: String, repeat: Boolean): LuaCodeTask {
        processedTasks.add(name)
        return super.createTask(code, name, repeat)
    }
}