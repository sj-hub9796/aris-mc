package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.ImageResource
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptImageRenderer(
    private var resource: ImageResource,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : BaseWidget(x, y, width, height), ILuaStaticDecl by LuaClientOnlyGenerated.ScriptImageRenderer_LuaGenerated {
    override fun RenderUtil.render(mx: Double, my: Double, delta: Float) {
        if (resource.isLoaded) {
            push {
                useTexture(resource) {
                    translate(x.toDouble(), y.toDouble(), 0.0)
                    render(0, 0, width, height)
                }
            }
        } else {
            // Image not loaded, TODO
        }
    }

    @LuaFunction
    fun dummy() {}
}