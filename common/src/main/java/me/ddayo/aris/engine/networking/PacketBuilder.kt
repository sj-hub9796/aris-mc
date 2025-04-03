package me.ddayo.aris.engine.networking

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.lua.glue.InitGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf

@LuaProvider(InitEngine.PROVIDER)
object PacketBuilderFunctions {

}

@LuaProvider(InitEngine.PROVIDER)
abstract class Packet(val id: String): ILuaStaticDecl by InitGenerated.Packet_LuaGenerated {
    private val subPackets = mutableMapOf<String, Packet>()

    @LuaFunction
    fun append(packet: Packet) {
        subPackets[packet.id] = packet
    }

    abstract fun process(buf: FriendlyByteBuf)
}

