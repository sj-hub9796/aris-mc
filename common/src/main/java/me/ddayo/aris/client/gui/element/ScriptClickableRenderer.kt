package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.math.Area
import me.ddayo.aris.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptClickableRenderer(
    private val clickHook: () -> Unit,
    private val area: Area
) : ILuaStaticDecl by LuaClientOnlyGenerated.ScriptClickableRenderer_LuaGenerated, BaseWidget(area.minX.toInt(), area.minY.toInt(), (area.maxX - area.minX).toInt(), (area.maxY - area.minY).toInt()) {
    override fun clicked(mx: Double, my: Double, button: Int) = if(area.isIn(mx with my) && isActive && isVisible) {
        clickHook()
        true
    }
    else false


    @LuaFunction
    fun dummy() {}
}