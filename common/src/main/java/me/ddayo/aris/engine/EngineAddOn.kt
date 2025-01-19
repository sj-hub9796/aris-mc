package me.ddayo.aris.engine

import dev.architectury.injectables.annotations.ExpectPlatform

object EngineAddOn {
    @JvmStatic
    @ExpectPlatform
    fun initEngineAddOns(): List<EngineInitializer> {
        throw NotImplementedError()
    }
}