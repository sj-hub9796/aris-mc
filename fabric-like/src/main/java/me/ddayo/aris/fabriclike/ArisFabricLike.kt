package me.ddayo.aris.fabriclike

import me.ddayo.aris.Aris
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

object ArisFabricLike {
    fun init() {
        Aris.init()
        ClientLifecycleEvents.CLIENT_STARTED.register {
            Aris.afterStart()
        }
    }
}
