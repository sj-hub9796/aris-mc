package me.ddayo.aris.client.gui.element

import me.ddayo.aris.client.lua.ClientFunction
import me.ddayo.aris.lua.glue.ScriptClickableRenderer_LuaGenerated.pushLua
import me.ddayo.aris.lua.math.Area
import me.ddayo.aris.lua.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.Lua

@LuaProvider(ClientFunction.CLIENT_ONLY)
class ScriptClickableRenderer(
    private val clickHook: () -> Unit,
    private val area: Area,
    component: Component
) : BaseWidget(area.minX.toInt(), area.minY.toInt(), (area.maxX - area.minX).toInt(), (area.maxY - area.minY).toInt(), component) {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        /* do nothing */
    }

    override fun clicked(d: Double, e: Double): Boolean {
        if(area.isIn(d with e) && active && visible) {
            clickHook()
            return true
        }
        return false
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        /* not planned */
    }

    @LuaFunction
    fun dummy() {}

    override fun toLua(lua: Lua) = pushLua(lua)
}