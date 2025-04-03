package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity

@LuaProvider(InGameEngine.PROVIDER)
open class LuaEntity(val inner: Entity): ILuaStaticDecl by InGameGenerated.LuaEntity_LuaGenerated {
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

    @LuaProperty
    open val x get() = inner.x

    @LuaProperty
    open val y get() = inner.y

    @LuaProperty
    open val z get() = inner.z
}