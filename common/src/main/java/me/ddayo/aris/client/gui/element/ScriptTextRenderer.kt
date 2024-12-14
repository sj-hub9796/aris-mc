package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.gui.RenderUtil.push
import me.ddayo.aris.client.lua.ClientFunction
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

@LuaProvider(ClientFunction.CLIENT_ONLY)
class ScriptDefaultTextRenderer(
    private var text: String,
    private val font: Font,
    x: Int,
    y: Int,
    private var scale: Double,
    private var color: Int,
    component: Component
) : BaseWidget(x, y, (font.width(text) * scale).toInt(), (font.lineHeight * scale).toInt(), component),
    ILuaStaticDecl by LuaClientOnlyGenerated.ScriptDefaultTextRenderer_LuaGenerated {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val stack = guiGraphics.pose()
        if (scale != 0.0)
            stack.push {
                stack.translate(x.toDouble(), y.toDouble(), 0.0)
                stack.scale(scale.toFloat(), scale.toFloat(), scale.toFloat())
                guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    text,
                    (x / scale).toInt(),
                    (y / scale).toInt(),
                    color
                )
            }
    }

    @LuaFunction("set_scale")
    fun setScale(new: Double) {
        scale = new
    }

    @LuaFunction("get_scale")
    fun getScale() = scale

    @LuaFunction("set_color")
    fun setColor(new: Int) {
        color = new
    }

    @LuaFunction("get_color")
    fun getColor() = color

    @LuaFunction("set_text")
    fun setText(new: String) {
        text = new
        width = font.width(text)
    }

    @LuaFunction("get_text")
    fun getText() = text

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {

    }
}