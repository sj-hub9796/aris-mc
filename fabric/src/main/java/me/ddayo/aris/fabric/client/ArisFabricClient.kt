package me.ddayo.aris.fabric.client

import me.ddayo.aris.fabriclike.client.C2SNetworking
import me.ddayo.aris.fabriclike.client.ArisFabricLikeClient
import net.fabricmc.api.ClientModInitializer

class ArisFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        ArisFabricLikeClient.init()
    }
}