package me.ddayo.aris.forge

import me.ddayo.aris.Aris
import me.ddayo.aris.client.ArisClient
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent
import net.minecraftforge.fml.loading.FMLEnvironment
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@net.minecraftforge.fml.common.Mod(Aris.MOD_ID)
class ArisForge {
    // TODO
    class AEvent: Event() {

    }
    @SubscribeEvent
    fun regA(e: AEvent) {
        LogManager.getLogger().info("test")
    }

    init {
        MOD_CONTEXT.getKEventBus().post(AEvent())
        MOD_CONTEXT.getKEventBus().register { it: FMLConstructModEvent ->
            it.enqueueWork {
                Aris.init()
                if (FMLEnvironment.dist.isClient)
                    ArisClient.init()
            }
        }

        ArisForgeNetworking.register()
    }
}
