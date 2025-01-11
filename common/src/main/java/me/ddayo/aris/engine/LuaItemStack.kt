package me.ddayo.aris.engine

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.lua.glue.LuaGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.world.item.ItemStack

@LuaProvider
class LuaItemStack(val inner: ItemStack) : ILuaStaticDecl by LuaGenerated.LuaItemStack_LuaGenerated {
    @LuaProperty(name = "count")
    var count
        get() = inner.count
        set(value) {
            inner.count = value
        }

    @LuaProperty(name = "display_name")
    val displayName get() = inner.displayName

    @LuaProperty(name = "name")
    val name get() = inner.item.defaultInstance.displayName.string
}