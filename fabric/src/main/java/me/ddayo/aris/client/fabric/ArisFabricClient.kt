package me.ddayo.aris.client.fabric

import me.ddayo.aris.client.fabriclike.ArisFabricLikeClient
import net.fabricmc.api.ClientModInitializer

class ArisFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        ArisFabricLikeClient.init()
    }
}