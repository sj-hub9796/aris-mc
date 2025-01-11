package me.ddayo.aris.client.engine.functions

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.ClientDataHandler
import me.ddayo.aris.client.engine.ClientInGameEngine
import me.ddayo.aris.client.gui.HudRenderer
import me.ddayo.aris.engine.LuaItemStack
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack


@LuaProvider(ClientInGameEngine.PROVIDER)
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

    private val tickFunctions = mutableListOf<LuaFunc>()
    @LuaFunction("add_tick_hook")
    fun addTickHook(f: LuaFunc) {
        tickFunctions.add(f)
    }

    fun tick() {
        tickFunctions.forEach { it.call() }
    }


    @LuaFunction("create_hud")
    fun createHud() = HudRenderer()

    @LuaFunction("clear_opened_hud")
    fun clearHud() = ClientDataHandler.enabledHud.clear()

    @LuaFunction("remote_string_data")
    fun getStringData(of: String) = ClientDataHandler.clientStringData[of] ?: "null"

    @LuaFunction("remote_number_data")
    fun getNumberData(of: String) = ClientDataHandler.clientNumberData[of] ?: 0.0

    @LuaFunction("remove_item_data")
    fun getItemData(of: String) = LuaItemStack(ClientDataHandler.clientItemStackData[of] ?: ItemStack.EMPTY)

}