package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.element.BaseWidget
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated

@LuaProvider(ClientMainEngine.PROVIDER)
abstract class BaseRenderer: ILuaStaticDecl by LuaClientOnlyGenerated.BaseRenderer_LuaGenerated {
    @JvmName("_render")
    fun render(r: RenderUtil, mx: Double, my: Double, delta: Float) {
        renderHooks.indices.forEach {
            renderHooks[it].call(mx, my, delta)
        }
        addedWidgets.indices.forEach {
            addedWidgets[it].render(r, mx, my, delta)
        }
        r.render(mx, my, delta)
    }

    protected val addedWidgets = mutableListOf<BaseWidget>()

    @LuaFunction(name = "add_child")
    fun addChild(child: BaseWidget) {
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

    open fun RenderUtil.render(mx: Double, my: Double, delta: Float) {}
}