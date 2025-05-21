package me.ddayo.aris.client.gui.element

import com.mojang.blaze3d.systems.RenderSystem
import me.ddayo.aris.luagen.ILuaStaticDecl
import me.ddayo.aris.luagen.LuaMultiReturn
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.client.gui.BaseRectComponent
import me.ddayo.aris.client.gui.ImageResource
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.renderer.GameRenderer

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

    private var th1 = 0.0
    private var th2 = 1.0
    private var tv1 = 0.0
    private var tv2 = 1.0

    @LuaFunction("get_uv1")
    fun getUV1() = LuaMultiReturn(th1, tv1)

    @LuaFunction("get_uv2")
    fun getUV2() = LuaMultiReturn(th2, tv2)

    @LuaFunction("set_uv1")
    fun setUV1(u: Double, v: Double) {
        th1 = u
        tv1 = v
    }

    @LuaFunction("set_uv2")
    fun setUV2(u: Double, v: Double) {
        th2 = u
        tv2 = v
    }

    @LuaFunction("crop_uv1")
    fun cropUV1(u: Double, v: Double) {
        x = x - width / th1 + width / u
        y = y - height / tv1 + height / v
        width = width * (th2 - u) / (th2 - th1)
        height = height * (tv2 - v) / (tv2 - tv1)
        th1 = u
        tv1 = v
    }

    @LuaFunction("crop_uv2")
    fun cropUV2(u: Double, v: Double) {
        width = width * (u - th1) / (th2 - th1)
        height = height * (v - tv1) / (tv2 - tv1)
        th2 = u
        tv2 = v
    }

    override fun RenderUtil._render(mx: Double, my: Double, delta: Float) {
        if (resource.isLoaded) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader)
            useTexture(resource) {
                if (!imageLoaded) {
                    imageLoaded = true
                    if (width == 0.0)
                        width = getTexWidth().toDouble()
                    if (height == 0.0)
                        height = getTexHeight().toDouble()
                }

                _render(0.0, 0.0, 1.0, 1.0, th1, th2, tv1, tv2)
            }
        } else {
            // Image not loaded, TODO
        }
    }
}