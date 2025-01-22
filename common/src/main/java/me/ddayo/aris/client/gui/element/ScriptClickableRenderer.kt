package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.client.gui.BaseComponent
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.math.Area
import me.ddayo.aris.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptClickableRenderer(
    private val clickHook: () -> Unit,
    @LuaProperty
    var area: Area
) : ILuaStaticDecl by LuaClientOnlyGenerated.ScriptClickableRenderer_LuaGenerated, BaseComponent(), IClickableElement {
    override val isScaleRateFixed = true
    override fun clicked(mx: Double, my: Double, button: Int) = if(area.isIn(mx with my) && isActive && isVisible) {
        clickHook()
        true
    }
    else false
}
