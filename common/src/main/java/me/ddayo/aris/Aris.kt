package me.ddayo.aris

import org.apache.logging.log4j.LogManager

object Aris {
    const val MOD_ID = "aris"
    fun init() {
        LogManager.getLogger().info("Hello, world")
    }
}