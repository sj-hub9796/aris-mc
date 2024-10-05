package me.ddayo.aris.lua

import me.ddayo.aris.lua.math.AreaBuilder
import me.ddayo.aris.lua.math.Point
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import org.apache.logging.log4j.LogManager

@LuaProvider
object BaseFunction {
    @LuaFunction(name = "create_area_builder")
    fun create() = AreaBuilder()

    @LuaFunction("create_point")
    fun create(x: Double, y: Double) = Point(x, y)

    @LuaFunction("log_debug")
    fun debugLog(msg: String) = LogManager.getLogger().debug(msg)

    @LuaFunction("log_info")
    fun infoLog(msg: String) = LogManager.getLogger().info(msg)

    @LuaFunction("log_warn")
    fun warnLog(msg: String) = LogManager.getLogger().warn(msg)

    @LuaFunction("log_error")
    fun errorLog(msg: String) = LogManager.getLogger().error(msg)
}