package me.ddayo.aris.engine.networking

import io.netty.buffer.Unpooled
import me.ddayo.aris.Aris
import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.lua.glue.InitGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

/**
 * This file introduce how to add data into packet
 * You may do:
 * 1. declare custom function that accepts custom argument
 * 2. declare custom function that pack data into builder
 */
@LuaProvider(InitEngine.PROVIDER, library = "aris.init.networking")
object PacketBuilderFunctions {
    @LuaFunction("integer_arg")
    fun integerArg(of: String) = object: AbstractPackableData<Int>(ResourceLocation(Aris.MOD_ID, of)) {
        override fun process(buf: FriendlyByteBuf) = buf.readInt()
        override fun intoPacket(buf: FriendlyByteBuf, data: Int) {
            buf.writeInt(data)
        }
    }

    @LuaFunction("float_arg")
    fun floatArg(of: String) = object: AbstractPackableData<Double>(ResourceLocation(Aris.MOD_ID, of)) {
        override fun process(buf: FriendlyByteBuf) = buf.readDouble()
        override fun intoPacket(buf: FriendlyByteBuf, data: Double) {
            buf.writeDouble(data)
        }
    }

    @LuaFunction("string_arg")
    fun stringArg(of: String) = object: AbstractPackableData<String>(ResourceLocation(Aris.MOD_ID, of)) {
        override fun process(buf: FriendlyByteBuf) = buf.readUtf()
        override fun intoPacket(buf: FriendlyByteBuf, data: String) {
            buf.writeUtf(data)
        }
    }
}

abstract class AbstractPackableData<T>(val id: ResourceLocation) {
    abstract fun intoPacket(buf: FriendlyByteBuf, data: T)
    abstract fun process(buf: FriendlyByteBuf): T
}

@LuaProvider(InitEngine.PROVIDER)
abstract class PacketDeclaration(val id: ResourceLocation): ILuaStaticDecl by InitGenerated.PacketDeclaration_LuaGenerated {
    protected val subPackets = mutableMapOf<ResourceLocation, AbstractPackableData<*>>()

    @LuaFunction
    fun append(packet: AbstractPackableData<*>) {
        subPackets[packet.id] = packet
    }

    val frozen by lazy { subPackets.toList().sortedBy { it.first } }

    fun buildBuffer(of: Builder) = FriendlyByteBuf(Unpooled.buffer()).apply {
        writeResourceLocation(id)
        for((id, instance) in frozen) {
            @Suppress("UNCHECKED_CAST")
            (instance as AbstractPackableData<Any?>).intoPacket(this, of.inner[id])
        }
    }

    abstract fun parse(buf: FriendlyByteBuf): Array<Pair<ResourceLocation, Any?>>
    abstract fun getFunction(): LuaFunc?

    @LuaProvider(InGameEngine.PROVIDER)
    @LuaProvider(ClientInGameEngine.PROVIDER)
    public inner class Builder: ILuaStaticDecl by InGameGenerated.Builder_LuaGenerated {
        val inner = mutableMapOf<ResourceLocation, Any?>()
        // @LuaFunction
        fun append(id: String, of: Any?) {
            inner[ResourceLocation(Aris.MOD_ID, id)] = of
        }
        @LuaFunction("append_int")
        fun appendInt(id: String, of: Int) {
            append(id, of)
        }
        @LuaFunction("append_string")
        fun appendString(id: String, of: String) {
            append(id, of)
        }
        @LuaFunction("append_float")
        fun appendFloat(id: String, of: Double) {
            append(id, of)
        }

        fun build() = buildBuffer(this)
    }
}

