package me.ddayo.aris.engine.networking

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation


typealias BufferProcessor<T> = (buf: FriendlyByteBuf) -> T

@LuaProvider(InGameEngine.PROVIDER)
class S2CPacket(id: ResourceLocation) {
    val args = mutableListOf<BufferProcessor<*>>()
}

@LuaProvider(InitEngine.PROVIDER)
object S2CPacketHandler {
    val packets = mutableListOf<S2CPacket>()

    @LuaFunction("create_s2c_packet")
    fun createS2CPacket(id: ResourceLocation) {
        packets.add(S2CPacket(id))
    }

    @JvmStatic
    @ExpectPlatform
    fun registerPackets() {
        throw NotImplementedError()
    }
}