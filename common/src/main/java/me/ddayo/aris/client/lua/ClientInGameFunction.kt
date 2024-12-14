package me.ddayo.aris.client.lua

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.ClientDataHandler
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack


@LuaProvider(ClientInGameFunction.CLIENT_IN_GAME_ONLY)
object ClientInGameFunction {
    private val mc by lazy { Minecraft.getInstance() }
    const val CLIENT_IN_GAME_ONLY = "ClientInGameOnlyGenerated"
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

    @LuaFunction("get_server_data_string")
    fun getServerDataString(of: String) = ClientDataHandler.clientStringData[of] ?: "null"

    @LuaFunction("get_server_data_number")
    fun getServerDataNumber(of: String) = ClientDataHandler.clientNumberData[of] ?: 0.0

    @LuaFunction("get_server_data_item")
    fun getServerDataItem(of: String) = ClientDataHandler.clientItemStackData[of] ?: ItemStack.EMPTY

    private val tickFunctions = mutableListOf<LuaFunc>()
    @LuaFunction("add_tick_hook")
    fun addTickHook(f: LuaFunc) {
        tickFunctions.add(f)
    }

    fun tick() {
        tickFunctions.forEach { it.call() }
    }
}