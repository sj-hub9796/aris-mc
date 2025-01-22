package me.ddayo.aris.client.fabriclike

import me.ddayo.aris.Aris
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.fabriclike.S2CNetworking
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
                    S2CNetworking.EngineSpace.GLOBAL -> ClientMainEngine.INSTANCE
                    S2CNetworking.EngineSpace.IN_GAME -> ClientInGameEngine.INSTANCE
                }
                engine?.createTask(File("robots/functions", of), name.ifEmpty { null })
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
                ClientInGameEngine.INSTANCE?.let {
                    when (type) {
                        S2CNetworking.ScriptDataType.STRING -> it.clientStringData[of] = data as String
                        S2CNetworking.ScriptDataType.NUMBER -> it.clientNumberData[of] = data as Double
                        S2CNetworking.ScriptDataType.ITEM -> it.clientItemStackData[of] =
                            data as ItemStack
                    }
                }
            }
        }
    }
}