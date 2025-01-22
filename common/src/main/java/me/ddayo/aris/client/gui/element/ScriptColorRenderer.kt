package me.ddayo.aris.client.gui.element

import com.mojang.blaze3d.systems.RenderSystem
import me.ddayo.aris.client.gui.BaseRectComponent
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.renderer.GameRenderer

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptColorRenderer(
    @LuaProperty
    var r: Int,
    @LuaProperty
    var g: Int,
    @LuaProperty
    var b: Int,
    @LuaProperty
    var a: Int
) : BaseRectComponent() {
    @LuaProperty
    var color: Long
        get() = (r.toLong() shl 16) or (g.toLong() shl 8) or b.toLong() or (a.toLong() shl 24)
        set(value) {
            r = ((value and 0xff0000) shr 16).toInt()
            g = ((value and 0xff00) shr 8).toInt()
            b = (value and 0xff).toInt()
            a = ((value and 0xff000000) shr 24).toInt()
        }

    constructor(color: Long) : this(((color and 0xff0000) shr 16).toInt(), ((color and 0xff00) shr 8).toInt(), (color and 0xff).toInt(), ((color and 0xff000000) shr 24).toInt())
    override fun RenderUtil._render(mx: Double, my: Double, delta: Float) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        fillRender(0, 0, 1, 1, r, g, b, a)
    }
}