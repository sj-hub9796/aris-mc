package me.ddayo.aris.client.forgelike

import me.ddayo.aris.client.ArisClient
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.multiplayer.ServerData
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.LevelTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber(modid = "aris", bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ArisForgeMod {
    @SubscribeEvent
    fun onLoadComplete(event: FMLLoadCompleteEvent) {
        // Called once after client has fully loaded
        ArisClient.onClientStart()
    }
}

@Mod.EventBusSubscriber(modid = "aris", bus = Mod.EventBusSubscriber.Bus.FORGE, value = [Dist.CLIENT])
object ArisForgeClientEvents {
    /** Fired once when the client has fully logged into a world. */
    @SubscribeEvent
    fun onPlayerLoggedIn(evt: ClientPlayerNetworkEvent.LoggingIn) {
        ArisClient.onClientJoinGame()
    }

    /** Fired once when the client logs out of a world (or quits to menu). */
    @SubscribeEvent
    fun onPlayerLoggedOut(evt: ClientPlayerNetworkEvent.LoggingOut) {
        ArisClient.onClientLeaveGame()
    }

    @SubscribeEvent
    fun onClientWorldTick(event: TickEvent.LevelTickEvent) {
        if(event.phase == TickEvent.Phase.END || event.side != LogicalSide.CLIENT) {
            ArisClient.clientWorldTick()
        }
    }

    /** Your per-frame client tick logic. */
    @SubscribeEvent
    fun onClientTick(evt: TickEvent.ClientTickEvent) {
        if (evt.phase == TickEvent.Phase.END) {
            ArisClient.clientTick()
        }
    }

    @SubscribeEvent
    fun onRenderGuiOverlay(event: RenderGuiOverlayEvent.Pre) {
        val graphics: GuiGraphics = event.guiGraphics
        val delta = event.partialTick
        RenderUtil.renderer.loadMatrix(graphics) {
            ClientInGameEngine.INSTANCE?.enabledHud?.mutableForEach {
                it.render(this, 0.0, 0.0, delta)
            }
        }
    }
}
