package me.ddayo.aris.client.fabriclike

import me.ddayo.aris.Aris
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.engine.networking.S2CPacketHandler
import me.ddayo.aris.fabriclike.ServerNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import java.io.File

object ClientNetworking {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ResourceLocation(Aris.MOD_ID, "open_script")) { client, handler, packet, sender ->
            val _operation = packet.readInt()
            val _space = packet.readInt()
            val _of = packet.readUtf()
            val _name = packet.readUtf()

            client.execute {
                val operation = ServerNetworking.Operation.entries[_operation]
                val space = ServerNetworking.EngineSpace.entries[_space]
                val of = _of
                val name = _name

                val engine = when (space) {
                    ServerNetworking.EngineSpace.GLOBAL -> ClientMainEngine.INSTANCE
                    ServerNetworking.EngineSpace.IN_GAME -> ClientInGameEngine.INSTANCE
                }
                engine?.createTask(File("robots/functions", of), name.ifEmpty { null })
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(ResourceLocation(Aris.MOD_ID, "sync_data")) { client, handler, packet, sender ->
            val of = packet.readUtf()
            val type = ServerNetworking.ScriptDataType.entries[packet.readByte().toInt()]
            val data: Any = when(type) {
                ServerNetworking.ScriptDataType.STRING -> packet.readUtf()
                ServerNetworking.ScriptDataType.NUMBER -> packet.readDouble()
                ServerNetworking.ScriptDataType.ITEM -> packet.readItem()
            }
            client.execute {
                ClientInGameEngine.INSTANCE?.let {
                    when (type) {
                        ServerNetworking.ScriptDataType.STRING -> it.clientStringData[of] = data as String
                        ServerNetworking.ScriptDataType.NUMBER -> it.clientNumberData[of] = data as Double
                        ServerNetworking.ScriptDataType.ITEM -> it.clientItemStackData[of] =
                            data as ItemStack
                    }
                }
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(ResourceLocation(Aris.MOD_ID, "generic_s2c")) { client, handler, buffer, sender ->
            val of = buffer.readResourceLocation()
            val packet = S2CPacketHandler.packets[of]!!
            val parsed = packet.parse(buffer)
            client.execute {
                packet.execute(parsed)
            }
        }
    }
}