package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.gui.element.BaseWidget
import me.ddayo.aris.lua.glue.BaseScreen_LuaGenerated.pushLua
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.LogManager
import party.iroiro.luajava.Lua
import party.iroiro.luajava.value.LuaValue

@LuaProvider
class BaseScreen : Screen(Component.empty()), ILuaStaticDecl {
    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        if(minecraft?.level != null)
            renderBackground(guiGraphics)
        else guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0xff000000.toInt())

        guiGraphics.pose().pushPose()
        guiGraphics.pose().scale(3f, 3f, 3f)
        guiGraphics.drawString(Minecraft.getInstance().font, "Test", 0, 0, 0xffffff)
        guiGraphics.pose().popPose()

        renderHooks.forEach { it.call(i, j, f) }

        super.render(guiGraphics, i, j, f)
    }

    @LuaFunction(name = "open")
    fun openScreen() {
        Minecraft.getInstance().setScreen(this)
    }

    override fun init() {
        addedWidgets.forEach {
            addRenderableWidget(it)
        }
        super.init()
    }

    override fun toLua(lua: Lua) = pushLua(lua)

    private val addedWidgets = mutableListOf<AbstractWidget>()

    @LuaFunction(name = "add_child")
    fun addChild(child: BaseWidget) {
        addedWidgets.add(child)
        rebuildWidgets()
    }

    private val renderHooks = mutableListOf<LuaValue>()
    @LuaFunction(name = "add_render_hook")
    fun addRenderHook(fn: LuaValue) {
        renderHooks.add(fn)
    }
}