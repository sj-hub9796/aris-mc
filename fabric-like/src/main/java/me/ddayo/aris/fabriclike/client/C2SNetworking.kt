package me.ddayo.aris.fabriclike.client

import me.ddayo.aris.Aris
import me.ddayo.aris.client.ClientDataHandler
import me.ddayo.aris.fabriclike.S2CNetworking
import me.ddayo.aris.lua.engine.EngineManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import java.io.File

object C2SNetworking {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ResourceLocation(Aris.MOD_ID, "open_script")) { client, handler, packet, sender ->
            val _operation = packet.readInt()
            val _space = packet.readInt()
            val _of = packet.readUtf()
            val _name = packet.readUtf()

            client.execute {
                val operation = S2CNetworking.Operation.entries[_operation]
                val space = S2CNetworking.EngineSpace.entries[_space]
                val of = _of
                val name = _name

                val engine = when (space) {
                    S2CNetworking.EngineSpace.GLOBAL -> EngineManager::retrieveGlobalEngine
                    S2CNetworking.EngineSpace.IN_GAME -> EngineManager::retrieveInGameEngine
                }
                engine.invoke {
                    it.createTask(File("robots", of), name.ifEmpty { null })
                }
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(ResourceLocation(Aris.MOD_ID, "sync_data")) { client, handler, packet, sender ->
            val of = packet.readUtf()
            val type = S2CNetworking.ScriptDataType.entries[packet.readByte().toInt()]
            val data: Any = when(type) {
                S2CNetworking.ScriptDataType.STRING -> packet.readUtf()
                S2CNetworking.ScriptDataType.NUMBER -> packet.readDouble()
                S2CNetworking.ScriptDataType.ITEM -> packet.readItem()
            }
            client.execute {
                when(type) {
                    S2CNetworking.ScriptDataType.STRING -> ClientDataHandler.clientStringData[of] = data as String
                    S2CNetworking.ScriptDataType.NUMBER -> ClientDataHandler.clientNumberData[of] = data as Double
                    S2CNetworking.ScriptDataType.ITEM -> ClientDataHandler.clientItemStackData[of] = data as ItemStack
                }
            }
        }
    }
}