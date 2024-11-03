package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.gui.ImageTexture
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.client.lua.ClientFunction
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

@LuaProvider(ClientFunction.CLIENT_ONLY)
class ScriptImageRenderer(
    private var resource: ImageTexture,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    component: Component
) : BaseWidget(x, y, width, height, component), ILuaStaticDecl by LuaClientOnlyGenerated.ScriptImageRenderer_LuaGenerated {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        resource.tryBind()
        resource.location?.let {
            RenderUtil.setTexture(it)
            guiGraphics.blit(it, x, y, 0, 0f, 0f, width, height, width, height)
            // RenderUtil.render(guiGraphics.pose().last().pose(), getX(), getY(), getWidth(), getHeight())
        } ?: run {
            // Loading in progress, TODO
        }
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        // not planned
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        return false
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean {
        return false
    }

    override fun mouseReleased(d: Double, e: Double, i: Int): Boolean {
        return false
    }

    @LuaFunction
    fun dummy() {}
}