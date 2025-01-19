package me.ddayo.aris.client.fabric

import me.ddayo.aris.client.fabriclike.ArisFabricLikeClient
import me.ddayo.aris.engine.EngineInitializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager

class ArisFabricClient: ClientModInitializer {
    companion object {
        lateinit var INSTANCE: ArisFabricClient
            private set
    }
    init {
        INSTANCE = this
    }

    val clientInitEngineAddOn = mutableListOf<EngineInitializer>()
    override fun onInitializeClient() {
        ArisFabricLikeClient.init()
        val fabricLoader = FabricLoader.getInstance()
        fabricLoader.getEntrypointContainers("aris-client-init", EngineInitializer::class.java)
            .forEach {
                LogManager.getLogger().info("AddOn for client-init engine registered: " + it.provider.metadata.id)
                clientInitEngineAddOn.add(it.entrypoint)
            }
    }
}