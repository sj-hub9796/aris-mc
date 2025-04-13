package me.ddayo.aris.fabriclike

import dev.architectury.injectables.annotations.ExpectPlatform
import me.ddayo.aris.Aris
import me.ddayo.aris.engine.networking.Packet
import me.ddayo.aris.engine.networking.S2CPacket
import me.ddayo.aris.engine.wrapper.LuaPlayerEntity
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.luagen.LuaFunction
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

object ServerNetworking {
    enum class Operation {
        OPEN,
        RELOAD,
        STOP
    }

    enum class EngineSpace {
        GLOBAL,
        IN_GAME
    }

    fun ServerPlayer.sendOpenScriptPacket(operation: Operation, space: EngineSpace, of: String, name: String = "") =
        ServerPlayNetworking.send(this, ResourceLocation(Aris.MOD_ID, "open_script"), PacketByteBufs.create().apply {
            writeInt(operation.ordinal)
            writeInt(space.ordinal)
            writeUtf(of)
            writeUtf(name)
        })

    enum class ScriptDataType {
        STRING,
        NUMBER,
        ITEM
    }

    fun ServerPlayer.sendDataPacket(of: String, data: Any) =
        ServerPlayNetworking.send(this, ResourceLocation(Aris.MOD_ID, "sync_data"), PacketByteBufs.create().apply {
            writeUtf(of)
            when(data) {
                is String -> {
                    writeByte(ScriptDataType.STRING.ordinal)
                    writeUtf(data)
                }
                is Number -> {
                    writeByte(ScriptDataType.NUMBER.ordinal)
                    writeDouble(data.toDouble())
                }
                is ItemStack -> {
                    writeByte(ScriptDataType.ITEM.ordinal)
                    writeItem(data)
                }
            }
        })

    fun register() {

    }
}