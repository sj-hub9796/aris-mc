package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.lua.glue.BaseScreen_LuaGenerated.pushLua
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import party.iroiro.luajava.Lua

@LuaProvider
class BaseScreen: Screen(Component.empty()), ILuaStaticDecl {
    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().scale(3f, 3f, 3f)
        guiGraphics.drawString(Minecraft.getInstance().font, "Test", 0, 0, 0xffffff)
        guiGraphics.pose().popPose()
        super.render(guiGraphics, i, j, f)
    }

    @LuaFunction(name = "open")
    fun openScreen() {
        Minecraft.getInstance().setScreen(this)
    }

    override fun toLua(lua: Lua) = pushLua(lua)
}