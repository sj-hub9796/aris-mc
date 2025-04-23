package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.lua.glue.LuaGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.world.item.ItemStack

@LuaProvider(InGameEngine.PROVIDER)
class LuaItemStack(val inner: ItemStack) : ILuaStaticDecl by InGameGenerated.LuaItemStack_LuaGenerated {
    /**
     * 해당 ItemStack의 수량을 설정하거나 가져옵니다.
     */
    @LuaProperty(name = "count")
    var count
        get() = inner.count
        set(value) {
            inner.count = value
        }

    /**
     * 해당 아이템의 표기된 이름을 가져옵니다.
     */
    @LuaProperty(name = "display_name")
    val displayName get() = inner.displayName.string

    /**
     * 해당 아이템의 기본 이름을 가져옵니다.
     */
    @LuaProperty(name = "name")
    val name get() = inner.item.defaultInstance.displayName.string
}