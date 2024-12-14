package me.ddayo.aris.client

import me.ddayo.aris.client.lua.ClientInGameFunction
import me.ddayo.aris.lua.engine.ClientBaseEngine
import me.ddayo.aris.lua.engine.EngineManager
import net.minecraft.client.Minecraft
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.luajit.LuaJit

object ArisClient {
    fun init() {

    }

    fun clientTick() {
        EngineManager.retrieveGlobalEngine { it.loop() }
    }

    fun clientWorldTick() {
        ClientInGameFunction.tick()
        EngineManager.retrieveInGameEngine { it.loop() }
    }

    fun onClientJoinGame() {
        EngineManager.initInGameEngine()
    }

    fun onClientLeaveGame() {
        EngineManager.disposeInGameEngine()
    }

    fun onClientClose() {
        EngineManager.disposeGlobalEngine()
    }

    fun onClientStart() {
        EngineManager.initGlobalEngine()

        LogManager.getLogger().info("AfterStart")
        val engine = ClientBaseEngine(LuaJit())
        val task = engine.createTask(
            """
            check_version("0.1.0")
            local screen = create_window()
            screen:open()
            
            local t1 = create_default_text_renderer("Hello, world", 0, 0, 0.5, 0xffffff)
            local test_image = load_image("test", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png")
            local imr = create_image_renderer(test_image, 0,0,100,100)
            screen:add_child(imr)
            screen:add_child(create_clickable(function()
                log_info("Hello")
            end, 0, 0, 100, 100))
            screen:add_render_hook(function(x, y)
                imr:set_x(x)
                imr:set_y(y)
                t1:set_scale(t1:get_scale() + 0.01)
            end)
            local imr2 = create_image_renderer(test_image, 0,0,100,100)
            screen:add_child(imr2)
            screen:add_child(t1)
            while true do
                task_yield()
            end
        """.trimIndent(), "test"
        )
        // engine.loop()
        if (task.errorMessage.isNotBlank())
            LogManager.getLogger().error(task.pullError().toString())
    }
}