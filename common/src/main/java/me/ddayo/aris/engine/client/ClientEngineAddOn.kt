package me.ddayo.aris.engine.client

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.engine.EngineInitializer

object ClientEngineAddOn {
    @ExpectPlatform
    @JvmStatic
    fun clientInitEngineAddOns(): List<EngineInitializer> {
        throw NotImplementedError()
    }
}