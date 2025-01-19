package me.ddayo.aris.engine.client.fabric

import me.ddayo.aris.client.fabric.ArisFabricClient
import me.ddayo.aris.engine.EngineInitializer
import me.ddayo.aris.fabric.ArisFabric

object ClientEngineAddOnImpl {
    @JvmStatic
    fun clientInitEngineAddOns(): List<EngineInitializer> = ArisFabricClient.INSTANCE.clientInitEngineAddOn
}