package me.ddayo.aris.engine.networking

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.Aris
import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.lua.glue.InitGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager


@LuaProvider(InitEngine.PROVIDER)
class S2CPacketDeclaration(id: ResourceLocation) : PacketDeclaration(id), ILuaStaticDecl by InitGenerated.S2CPacketDeclaration_LuaGenerated {
    override fun parse(buf: FriendlyByteBuf): Array<Pair<ResourceLocation, Any?>> {
        return frozen.map { it.first to it.second.process(buf) }.toTypedArray()
    }

    override fun getFunction() = ClientInGameEngine.INSTANCE!!.packetFunctions[id]

    fun execute(parsed: Array<Pair<ResourceLocation, Any?>>) {
        getFunction()?.callAsTaskRawArg { task ->
            task.coroutine.newTable()
            for((rl, act) in parsed)
                if(engine.luaMain.pushNoInline(task.coroutine, act) == 1)
                    task.coroutine.setField(-2, rl.path)
            1
        } ?: run {
            LogManager.getLogger().error("Not declared packet $id")
        }
    }
}

@LuaProvider(InGameEngine.PROVIDER, library = "aris.game.networking")
object S2CPacketSenderHandler {
    @LuaFunction("send_s2c_packet")
    @ExpectPlatform
    @JvmStatic
    fun sendS2CPacket(player: LuaServerPlayer, packet: PacketDeclaration.Builder) {
        throw NotImplementedError()
    }

    @LuaFunction("create_s2c_packet_builder")
    fun createPacketBuilder(of: String): PacketDeclaration.Builder {
        return S2CPacketHandler.packets[ResourceLocation(Aris.MOD_ID, of)]!!.Builder()
    }
}

@LuaProvider(ClientInGameEngine.PROVIDER, library = "aris.game.client.networking")
object S2CPacketReceiverHandler {
    @LuaFunction("register_s2c_packet_handler")
    fun registerHandler(id: String, func: LuaFunc) {
        ClientInGameEngine.INSTANCE!!.packetFunctions[ResourceLocation(Aris.MOD_ID, id)] = func
    }
}

@LuaProvider(InitEngine.PROVIDER, library = "aris.init.networking")
object S2CPacketHandler {
    val packets = mutableMapOf<ResourceLocation, S2CPacketDeclaration>()

    @LuaFunction("create_s2c_packet")
    fun createS2CPacket(_id: String): S2CPacketDeclaration {
        val id = ResourceLocation(Aris.MOD_ID, _id)
        val packet = S2CPacketDeclaration(id)
        packets[id] = packet
        return packet
    }
}