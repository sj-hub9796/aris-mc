package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand


@LuaProvider(InGameEngine.PROVIDER)
class LuaServerPlayer(private val player: ServerPlayer) :
    ILuaStaticDecl by InGameGenerated.LuaServerPlayer_LuaGenerated {
    @LuaProperty("name")
    val name get() = player.name.string

    @LuaProperty("display_name")
    val displayName get() = player.displayName.string

    @LuaProperty("custom_name")
    var customName
        get() = player.customName?.string ?: "None"
        set(value) {
            player.customName = Component.literal(value)
        }

    @LuaProperty("main_hand_item")
    var mainHandItem
        get() = LuaItemStack(player.mainHandItem)
        set(value) {
            player.setItemInHand(InteractionHand.MAIN_HAND, value.inner)
        }
}