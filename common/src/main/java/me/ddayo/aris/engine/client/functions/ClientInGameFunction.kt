package me.ddayo.aris.engine.client.functions

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.gui.HudRenderer
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.luagen.RetrieveEngine
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack


@LuaProvider(ClientInGameEngine.PROVIDER, library = "aris")
object ClientInGameFunction {
    private val mc by lazy { Minecraft.getInstance() }
    @LuaFunction("send_system_message")
    fun sendSystemMessage(message: String) {
        mc.player!!.sendSystemMessage(Component.literal(message))
    }

    @LuaFunction("invoke_command")
    fun invokeCommand(command: String) {
        mc.player!!.connection.sendCommand(command)
    }

    @LuaFunction("get_player_x")
    fun getPlayerX() = mc.player!!.x

    @LuaFunction("get_player_y")
    fun getPlayerY() = mc.player!!.y

    @LuaFunction("add_tick_hook")
    fun addTickHook(@RetrieveEngine engine: ClientInGameEngine, f: LuaFunc) {
        engine.tickFunctions.add(f)
    }


    @LuaFunction("create_hud")
    fun createHud() = HudRenderer()

    @LuaFunction("clear_opened_hud")
    fun clearHud(@RetrieveEngine engine: ClientInGameEngine) = engine.enabledHud.clear()

    @LuaFunction("remote_string_data")
    fun getStringData(@RetrieveEngine engine: ClientInGameEngine, of: String) = engine.clientStringData[of] ?: "null"

    @LuaFunction("remote_number_data")
    fun getNumberData(@RetrieveEngine engine: ClientInGameEngine, of: String) = engine.clientNumberData[of] ?: 0.0

    @LuaFunction("remove_item_data")
    fun getItemData(@RetrieveEngine engine: ClientInGameEngine, of: String) = LuaItemStack(engine.clientItemStackData[of] ?: ItemStack.EMPTY)

    @LuaFunction("add_on_key_pressed")
    fun onKeyPressed(@RetrieveEngine engine: ClientInGameEngine, key: String, function: LuaFunc) = engine.registerKeyHook(key, function)
}