package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.gui.Font

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptDefaultTextRenderer(
    _text: String,
    private val font: Font,
    x: Int,
    y: Int,
    @LuaProperty
    var scale: Double,
    @LuaProperty
    var color: Int
) : BaseWidget(x, y, (font.width(_text) * scale).toInt(), (font.lineHeight * scale).toInt()),
    ILuaStaticDecl by LuaClientOnlyGenerated.ScriptDefaultTextRenderer_LuaGenerated {

    @LuaProperty
    var text = _text
        set(value) {
            field = value
            width = font.width(text)
        }

    override fun RenderUtil.render(mx: Double, my: Double, delta: Float) {
        if (scale != 0.0)
            push {
                translate(x.toDouble(), y.toDouble(), 0.0)
                scale(scale, scale, scale)

                graphics.drawString(
                    font,
                    text,
                    (x / scale).toInt(),
                    (y / scale).toInt(),
                    color
                )
            }
    }
}