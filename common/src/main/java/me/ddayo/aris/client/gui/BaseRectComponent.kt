package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider

@LuaProvider(ClientMainEngine.PROVIDER)
open class BaseRectComponent : BaseComponent(),
    ILuaStaticDecl by LuaClientOnlyGenerated.BaseRectComponent_LuaGenerated {
    @LuaProperty("width")
    var width = 1.0
        set(value) {
            xScale = value / fixedWidth
            field = value
        }

    @LuaProperty("height")
    var height = 1.0
        set(value) {
            yScale = value / fixedHeight
            field = value
        }

    @LuaProperty("fixed_width")
    var fixedWidth = 1.0
        set(value) {
            xScale = width / value
            field = value
        }

    @LuaProperty("fixed_height")
    var fixedHeight = 1.0
        set(value) {
            yScale = height / value
            field = value
        }
}