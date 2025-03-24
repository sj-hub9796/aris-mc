package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

@LuaProvider(InGameEngine.PROVIDER)
open class LuaPlayerEntity(val inner: Player) : ILuaStaticDecl by InGameGenerated.LuaPlayerEntity_LuaGenerated {
    @LuaProperty("name")
    val name get() = inner.name.string

    @LuaProperty("display_name")
    val displayName get() = inner.displayName.string

    @LuaProperty("custom_name")
    var customName
        get() = inner.customName?.string ?: "None"
        set(value) {
            inner.customName = Component.literal(value)
        }

    @LuaProperty("main_hand_item")
    open val mainHandItem
        get() = LuaItemStack(inner.mainHandItem)

    @LuaProperty
    open val x get() = inner.x

    @LuaProperty
    open val y get() = inner.y

    @LuaProperty
    open val z get() = inner.z
}