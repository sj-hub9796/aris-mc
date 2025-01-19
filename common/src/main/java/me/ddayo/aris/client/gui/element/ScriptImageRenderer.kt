package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.BaseRectComponent
import me.ddayo.aris.client.gui.ImageResource
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptImageRenderer(
    @LuaProperty(name = "image")
    var resource: ImageResource
) : BaseRectComponent(), ILuaStaticDecl by LuaClientOnlyGenerated.ScriptImageRenderer_LuaGenerated {
    private var imageLoaded = false

    init {
        width = 0.0
        height = 0.0
    }

    override fun RenderUtil._render(mx: Double, my: Double, delta: Float) {
        if (resource.isLoaded) {
            useTexture(resource) {
                if (!imageLoaded) {
                    imageLoaded = true
                    if (width == 0.0)
                        width = getTexWidth().toDouble()
                    if (height == 0.0)
                        height = getTexHeight().toDouble()
                }

                _render(0.0, 0.0, 1.0, 1.0)
            }
        } else {
            // Image not loaded, TODO
        }
    }
}