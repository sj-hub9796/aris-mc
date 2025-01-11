package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.BaseRenderer
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
abstract class BaseWidget(
    @LuaProperty
    var x: Int,
    @LuaProperty
    var y: Int,
    @LuaProperty
    var width: Int,
    @LuaProperty
    var height: Int
) : ILuaStaticDecl by LuaClientOnlyGenerated.BaseWidget_LuaGenerated, BaseRenderer() {
    @LuaProperty(name = "is_visible")
    var isVisible = true
    @LuaProperty(name = "is_active")
    var isActive = true

    override fun RenderUtil.render(mx: Double, my: Double, delta: Float) {}
    open fun clicked(mx: Double, my: Double, button: Int) = false
}