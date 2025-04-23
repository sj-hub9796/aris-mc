package me.ddayo.aris.forge

import me.ddayo.aris.Aris
import me.ddayo.aris.client.ArisClient
import net.minecraft.world.item.Item
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
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
        Aris.init()
        if (FMLEnvironment.dist.isClient)
            ArisClient.init()

        MOD_CONTEXT.getKEventBus().post(AEvent())
        // Register custom networking handlers
        ArisForgeNetworking.register()
    }
}
