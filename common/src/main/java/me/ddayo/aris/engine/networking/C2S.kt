package me.ddayo.aris.engine.networking

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.Aris
import me.ddayo.aris.luagen.ILuaStaticDecl
import me.ddayo.aris.luagen.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.lua.glue.InitGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import org.apache.logging.log4j.LogManager


@LuaProvider(InitEngine.PROVIDER)
class C2SPacketDeclaration(id: ResourceLocation) : PacketDeclaration(id), ILuaStaticDecl by InitGenerated.C2SPacketDeclaration_LuaGenerated {
    override fun parse(buf: FriendlyByteBuf): Array<Pair<ResourceLocation, Any?>> {
        return frozen.map { it.first to it.second.process(buf) }.toTypedArray()
    }

    override fun getFunction() = InGameEngine.INSTANCE!!.packetFunctions[id]

    fun execute(player: ServerPlayer, parsed: Array<Pair<ResourceLocation, Any?>>) {
        getFunction()?.callAsTaskRawArg { task ->
            engine.luaMain.pushNoInline(task.coroutine, LuaServerPlayer(player))
            task.coroutine.newTable()
            for((rl, act) in parsed)
                if(engine.luaMain.pushNoInline(task.coroutine, act) == 1)
                    task.coroutine.setField(-2, rl.path)
            2
        } ?: run {
            LogManager.getLogger().error("Not declared packet $id")
        }
    }
}

@LuaProvider(ClientInGameEngine.PROVIDER, library = "aris.game.client.networking")
object C2SPacketSenderHandler {
    /**
     * 서버로 주어진 패킷을 전송합니다.
     * @param packet 패킷
     */
    @LuaFunction("send_c2s_packet")
    @ExpectPlatform
    @JvmStatic
    fun sendC2SPacket(packet: PacketDeclaration.Builder) {
        throw NotImplementedError()
    }

    /**
     * 서버로 전송할 패킷을 설정하는 빌더(builder)를 만듭니다.
     * @param of 전송할 패킷의 id
     */
    @LuaFunction("create_c2s_packet_builder")
    fun createPacketBuilder(of: String): PacketDeclaration.Builder {
        return C2SPacketHandler.packets[ResourceLocation(Aris.MOD_ID, of)]!!.Builder()
    }
}

@LuaProvider(InGameEngine.PROVIDER, library = "aris.game.networking")
object C2SPacketReceiverHandler {
    /**
     * 패킷이 클라이언트로부터 전송됐을때 실행할 함수를 지정합니다.
     * @param id 패킷 id
     * @param func 실행할 함수
     */
    @LuaFunction("register_c2s_packet_handler")
    fun registerHandler(id: String, func: LuaFunc) {
        InGameEngine.INSTANCE!!.packetFunctions[ResourceLocation(Aris.MOD_ID, id)] = func
    }
}

@LuaProvider(InitEngine.PROVIDER, library = "aris.init.networking")
object C2SPacketHandler {
    val packets = mutableMapOf<ResourceLocation, C2SPacketDeclaration>()

    /**
     * 패킷을 새로 생성합니다.
     * @param _id 패킷 id
     * @return 생성된 패킷 정의 Builder
     */
    @LuaFunction("create_c2s_packet")
    fun createC2SPacket(_id: String): C2SPacketDeclaration {
        val id = ResourceLocation(Aris.MOD_ID, _id)
        val packet = C2SPacketDeclaration(id)
        packets[id] = packet
        return packet
    }
}