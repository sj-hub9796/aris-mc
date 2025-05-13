package me.ddayo.aris.forge

import io.netty.buffer.Unpooled
import me.ddayo.aris.Aris
import me.ddayo.aris.client.ArisClient
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.networking.C2SPacketHandler
import me.ddayo.aris.engine.networking.S2CPacketHandler
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

object ArisForgeNetworking {
    private const val PROTOCOL_VERSION = "1"
    val CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation(Aris.MOD_ID, "channel"),
        { PROTOCOL_VERSION },
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    )

    fun register() {
        var idx = 0
        CHANNEL.registerMessage(idx++, SyncData::class.java, SyncData::encode, SyncData::decode, SyncData::handle)
        CHANNEL.registerMessage(idx++, GenericS2C::class.java, GenericS2C::encode, GenericS2C::decode, GenericS2C::handle)
        CHANNEL.registerMessage(idx++, GenericC2S::class.java, GenericC2S::encode, GenericC2S::decode, GenericC2S::handle)
        CHANNEL.registerMessage(idx++, ReloadEngine::class.java, ReloadEngine::encode, ReloadEngine::decode, ReloadEngine::handle)
    }

    enum class ScriptDataType { STRING, NUMBER, ITEM }

    fun sendDataPacketForge(player: ServerPlayer, of: String, data: Any) {
        val msg = when (data) {
            is String     -> SyncData(of, ScriptDataType.STRING, dataString = data)
            is Number     -> SyncData(of, ScriptDataType.NUMBER, dataNumber = data.toDouble())
            is ItemStack  -> SyncData(of, ScriptDataType.ITEM, dataItem = data)
            else -> return
        }
        CHANNEL.send(PacketDistributor.PLAYER.with { player }, msg)
    }

    fun sendReloadPacketForge(player: ServerPlayer) {
        CHANNEL.send(PacketDistributor.PLAYER.with{ player }, ReloadEngine())
    }

    data class SyncData(
        val of: String,
        val type: ScriptDataType,
        val dataString: String? = null,
        val dataNumber: Double? = null,
        val dataItem: ItemStack? = null
    ) {
        companion object {
            fun encode(msg: SyncData, buf: FriendlyByteBuf) {
                buf.writeUtf(msg.of)
                buf.writeByte(msg.type.ordinal)
                when (msg.type) {
                    ScriptDataType.STRING -> buf.writeUtf(msg.dataString)
                    ScriptDataType.NUMBER -> buf.writeDouble(msg.dataNumber!!)
                    ScriptDataType.ITEM   -> buf.writeItem(msg.dataItem)
                }
            }
            fun decode(buf: FriendlyByteBuf): SyncData {
                val of = buf.readUtf(32767)
                val type = ScriptDataType.values()[buf.readByte().toInt()]
                return when (type) {
                    ScriptDataType.STRING -> SyncData(of, type, dataString = buf.readUtf(32767))
                    ScriptDataType.NUMBER -> SyncData(of, type, dataNumber = buf.readDouble())
                    ScriptDataType.ITEM   -> SyncData(of, type, dataItem = buf.readItem())
                }
            }
            fun handle(msg: SyncData, ctx: Supplier<NetworkEvent.Context>) {
                ctx.get().enqueueWork {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                        Runnable {
                            ClientInGameEngine.INSTANCE?.let { engine ->
                                when (msg.type) {
                                    ScriptDataType.STRING -> engine.clientStringData[msg.of] = msg.dataString!!
                                    ScriptDataType.NUMBER -> engine.clientNumberData[msg.of] = msg.dataNumber!!
                                    ScriptDataType.ITEM   -> engine.clientItemStackData[msg.of] = msg.dataItem!!
                                }
                            }
                        }
                    }
                }
                ctx.get().packetHandled = true
            }
        }
    }

    data class GenericS2C(val key: ResourceLocation, val raw: ByteArray) {
        companion object {
            fun encode(msg: GenericS2C, buf: FriendlyByteBuf) {
                buf.writeResourceLocation(msg.key)
                buf.writeInt(msg.raw.size)
                buf.writeBytes(msg.raw)
            }
            fun decode(buf: FriendlyByteBuf): GenericS2C {
                val key = buf.readResourceLocation()
                val len = buf.readInt()
                val arr = ByteArray(len)
                buf.readBytes(arr)
                return GenericS2C(key, arr)
            }
            fun handle(msg: GenericS2C, ctx: Supplier<NetworkEvent.Context>) {
                ctx.get().enqueueWork {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                        Runnable {
                            val packet = S2CPacketHandler.packets[msg.key]!!
                            val pb = FriendlyByteBuf(Unpooled.wrappedBuffer(msg.raw))
                            val parsed = packet.parse(pb)
                            packet.execute(parsed)
                        }
                    }
                }
                ctx.get().packetHandled = true
            }
        }
    }

    data class GenericC2S(val key: ResourceLocation, val raw: ByteArray) {
        companion object {
            fun encode(msg: GenericC2S, buf: FriendlyByteBuf) {
                buf.writeResourceLocation(msg.key)
                buf.writeInt(msg.raw.size)
                buf.writeBytes(msg.raw)
            }
            fun decode(buf: FriendlyByteBuf): GenericC2S {
                val key = buf.readResourceLocation()
                val len = buf.readInt()
                val arr = ByteArray(len)
                buf.readBytes(arr)
                return GenericC2S(key, arr)
            }
            fun handle(msg: GenericC2S, ctx: Supplier<NetworkEvent.Context>) {
                ctx.get().enqueueWork {
                    val player = ctx.get().sender
                    val packet = C2SPacketHandler.packets[msg.key]!!
                    val pb = FriendlyByteBuf(Unpooled.wrappedBuffer(msg.raw))
                    val parsed = packet.parse(pb)
                    packet.execute(player!!, parsed)
                }
                ctx.get().packetHandled = true
            }
        }
    }

    class ReloadEngine {
        companion object {
            fun encode(msg: ReloadEngine, buf: FriendlyByteBuf) {}
            fun decode(buf: FriendlyByteBuf) = ReloadEngine()
            fun handle(msg: ReloadEngine, ctx: Supplier<NetworkEvent.Context>) {
                ctx.get().enqueueWork {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                        Runnable {
                            ArisClient.reloadEngine()
                        }
                    }
                }
                ctx.get().packetHandled = true
            }
        }
    }

}
