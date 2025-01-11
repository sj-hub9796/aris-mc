package me.ddayo.aris.client.fabriclike

import me.ddayo.aris.client.ArisClient
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object ArisFabricLikeClient {
    fun init() {
        ArisClient.init()
        ClientLifecycleEvents.CLIENT_STARTED.register {
            ArisClient.onClientStart()
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            ArisClient.onClientClose()
        }

        ClientPlayConnectionEvents.JOIN.register { handler, sender, client ->
            ArisClient.onClientJoinGame()
        }

        ClientPlayConnectionEvents.DISCONNECT.register { handler, client ->
            ArisClient.onClientLeaveGame()
        }

        ClientTickEvents.START_CLIENT_TICK.register {
            ArisClient.clientTick()
        }

        ClientTickEvents.START_WORLD_TICK.register {
            ArisClient.clientWorldTick()
        }
        C2SNetworking.register()
    }
}