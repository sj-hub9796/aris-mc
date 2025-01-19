package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.util.ListExtensions.mutableForEach

@LuaProvider(ClientMainEngine.PROVIDER)
open class BaseComponent : ILuaStaticDecl by LuaClientOnlyGenerated.BaseComponent_LuaGenerated {
    @LuaProperty
    var x = 0.0

    @LuaProperty
    var y = 0.0

    @LuaProperty(name = "is_scale_rate_fixed")
    open val isScaleRateFixed = false

    @LuaProperty(name = "scale_x")
    var xScale = 1.0
        set(value) {
            assert(!isScaleRateFixed)
            field = value
        }

    @LuaProperty(name = "scale_y")
    var yScale = 1.0
        set(value) {
            assert(!isScaleRateFixed)
            field = value
        }

    @LuaProperty(name = "scale")
    var scale: Double
        get() = run {
            assert(xScale == yScale)
            return xScale
        }
        set(value) {
            xScale = value
            yScale = value
        }

    @LuaProperty(name = "is_visible")
    var isVisible = true

    @LuaProperty(name = "is_active")
    var isActive = true

    open fun render(r: RenderUtil, mx: Double, my: Double, delta: Float) {
        if(!isVisible) return
        r.apply {
            push {
                translate(x, y, 0.0)
                scale(xScale, yScale, 1.0)

                val nmx = (mx - x) / xScale
                val nmy = (my - y) / yScale

                renderHooks.mutableForEach {
                    it.call(nmx, nmy, delta)
                }
                _render(nmx, nmy, delta)
                addedWidgets.mutableForEach {
                    it.render(r, nmx, nmy, delta)
                }
            }
        }
    }

    protected val addedWidgets = mutableListOf<BaseComponent>()

    @LuaFunction(name = "add_child")
    fun addChild(child: BaseComponent) {
        addedWidgets.add(child)
    }

    private val renderHooks = mutableListOf<LuaFunc>()

    @LuaFunction(name = "add_render_hook")
    fun addRenderHook(fn: LuaFunc) {
        renderHooks.add(fn)
    }

    @LuaFunction(name = "clear_render_hook")
    fun clearRenderHook() {
        renderHooks.clear()
    }

    @LuaFunction(name = "clear_child")
    fun clearChild() {
        addedWidgets.clear()
    }

    open fun RenderUtil._render(mx: Double, my: Double, delta: Float) {}
}