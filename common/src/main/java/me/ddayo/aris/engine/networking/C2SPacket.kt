package me.ddayo.aris.engine.networking

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.Aris
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation


@LuaProvider(InitEngine.PROVIDER, library = "aris.network")
object C2SPacketHandler {

    @LuaFunction("add_c2s_packet")
    fun addC2SPacket(packet: AbstractPackableData<*>) {
        TODO()
    }

    @JvmStatic
    @ExpectPlatform
    fun registerPackets() {
        throw NotImplementedError()
    }
}