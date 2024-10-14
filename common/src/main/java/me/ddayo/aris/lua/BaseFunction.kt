package me.ddayo.aris.lua

import me.ddayo.aris.Aris
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import org.apache.logging.log4j.LogManager

@LuaProvider
object BaseFunction {
    @LuaFunction("log_debug")
    fun debugLog(msg: String) = LogManager.getLogger().debug(msg)

    @LuaFunction("log_info")
    fun infoLog(msg: String) = LogManager.getLogger().info(msg)

    @LuaFunction("log_warn")
    fun warnLog(msg: String) = LogManager.getLogger().warn(msg)

    @LuaFunction("log_error")
    fun errorLog(msg: String) = LogManager.getLogger().error(msg)

    @LuaFunction("check_version")
    fun version(v: String) {
        if(Version.versionCompare(v, Aris.VERSION) > 0) throw Exception("Current script needs $v which is not capable with ARIS ${Aris.VERSION}")
    }
}