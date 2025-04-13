package me.ddayo.aris.fabric

import me.ddayo.aris.engine.EngineInitializer
import me.ddayo.aris.fabriclike.ArisFabricLike
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager

class ArisFabric: ModInitializer {
    companion object {
        lateinit var INSTANCE: ArisFabric
    }
    init {
        INSTANCE = this
    }

    val initEngineAddOn = mutableListOf<EngineInitializer>()
    override fun onInitialize() {
        val fabricLoader = FabricLoader.getInstance()
        fabricLoader.getEntrypointContainers("aris-init", EngineInitializer::class.java)
            .forEach {
                LogManager.getLogger().info("AddOn for init engine registered: " + it.provider.metadata.id)
                initEngineAddOn.add(it.entrypoint)
            }
        ArisFabricLike.init()
    }
}