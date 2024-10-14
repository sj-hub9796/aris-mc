package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.lua.ClientFunction
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component

@LuaProvider(ClientFunction.CLIENT_ONLY)
abstract class BaseWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    component: Component
) : AbstractWidget(x, y, width, height, component), ILuaStaticDecl {

    @LuaFunction(name = "set_x")
    override fun setX(new: Int) {
        super.setX(new)
    }

    @LuaFunction(name = "get_x")
    override fun getX(): Int {
        return super.getX()
    }

    @LuaFunction(name = "set_y")
    override fun setY(new: Int) { super.setY(new) }
    @LuaFunction(name = "get_y")
    override fun getY(): Int {
        return super.getY()
    }

    @LuaFunction(name = "set_width")
    override fun setWidth(new: Int) { super.setWidth(new) }
    @LuaFunction(name = "get_width")
    override fun getWidth(): Int {
        return super.getWidth()
    }

    @LuaFunction(name = "set_height")
    open fun setHeight(new: Int) { height = new }

    @LuaFunction(name = "get_height")
    override fun getHeight(): Int {
        return super.getHeight()
    }
}