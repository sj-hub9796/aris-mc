package me.ddayo.aris.engine.networking

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf


@LuaProvider(InitEngine.PROVIDER, library = "aris.network")
object C2SPacketHandler {
    val c2sPacket = object: Packet("C2S-Head") {
        override fun process(buf: FriendlyByteBuf) {

        }
    }

    @LuaFunction("add_c2s_packet")
    fun addC2SPacket(packet: Packet) {
        c2sPacket.append(packet)
    }

    @JvmStatic
    @ExpectPlatform
    fun registerPackets() {
        throw NotImplementedError()
    }
}