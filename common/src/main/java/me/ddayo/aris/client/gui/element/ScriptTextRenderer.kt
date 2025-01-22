package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.client.gui.BaseComponent
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.gui.Font

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptDefaultTextRenderer(
    @LuaProperty
    var text: String,
    private val font: Font,
    @LuaProperty
    var color: Int
) : BaseComponent(),
    ILuaStaticDecl by LuaClientOnlyGenerated.ScriptDefaultTextRenderer_LuaGenerated {
    override val isScaleRateFixed = true

    override fun RenderUtil._render(mx: Double, my: Double, delta: Float) {
        graphics.drawString(
            font,
            text,
            (x / scale).toInt(),
            (y / scale).toInt(),
            color
        )
    }
}