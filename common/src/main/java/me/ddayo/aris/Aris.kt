package me.ddayo.aris

import me.ddayo.aris.lua.engine.ClientBaseEngine
import org.apache.logging.log4j.LogManager

object Aris {
    const val MOD_ID = "aris"
    const val VERSION = "0.1.0"

    fun init() {
        LogManager.getLogger().info("Hello, world")
    }

    fun afterStart() {
        val engine = ClientBaseEngine()
        engine.addTask(
            LuaEngine.LuaTask(
                engine, """
            check_version("0.1.0")
            local screen = create_window()
            screen:open()
            local imr = create_image_renderer(load_image("test", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png"), 0,0,100,100)
            screen:add_child(imr)
            screen:add_child(create_clickable(function()
                log_info("Hello")
            end, 0, 0, 100, 100))
            screen:add_render_hook(function(x, y)
                imr:set_x(x)
                imr:set_y(y)
            end)
            local imr2 = create_image_renderer(load_image("test", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png"), 0,0,100,100)
            screen:add_child(imr2)
            while true do
                task_yield()
            end
        """.trimIndent(), "test"
            )
        )
        engine.loop()
    }
}