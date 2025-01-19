package me.ddayo.aris.engine.fabric

import me.ddayo.aris.engine.EngineInitializer
import me.ddayo.aris.fabric.ArisFabric

object EngineAddOnImpl {
    @JvmStatic
    fun initEngineAddOns(): List<EngineInitializer> = ArisFabric.INSTANCE.initEngineAddOn
}