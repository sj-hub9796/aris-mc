package me.ddayo.aris.engine

import me.ddayo.aris.Aris
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import org.apache.logging.log4j.LogManager

@LuaProvider
object BaseFunction {
    private val logger = LogManager.getLogger()
    @LuaFunction("log_debug")
    fun debugLog(msg: String) = logger.debug(msg)

    @LuaFunction("log_info")
    fun infoLog(msg: String) = logger.info(msg)

    @LuaFunction("log_warn")
    fun warnLog(msg: String) = logger.warn(msg)

    @LuaFunction("log_error")
    fun errorLog(msg: String) = logger.error(msg)

    @LuaFunction("check_version")
    fun version(v: String) {
        if(Version.versionCompare(v, Aris.VERSION) > 0) throw Exception("Current script needs $v which is not capable with ARIS ${Aris.VERSION}")
    }
}