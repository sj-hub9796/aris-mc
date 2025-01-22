package me.ddayo.aris.forge

import me.ddayo.aris.Aris
import net.minecraft.world.item.Item
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
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
        MOD_CONTEXT.getKEventBus().post(AEvent())
        MOD_CONTEXT.getKEventBus().addListener { it: FMLCommonSetupEvent ->
            DeferredRegister.create(ForgeRegistries.ITEMS, "aris").apply {
                register("test") { Item(Item.Properties()) }
            }.register(MOD_CONTEXT.getKEventBus())
        }
    }
}
