package me.ddayo.aris.client.lua

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component


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

    private val tickFunctions = mutableListOf<LuaFunc>()
    @LuaFunction("add_tick_hook")
    fun addTickHook(f: LuaFunc) {
        tickFunctions.add(f)
    }

    fun tick() {
        tickFunctions.forEach { it.call() }
    }
}