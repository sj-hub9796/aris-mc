package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import org.apache.logging.log4j.LogManager
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@LuaProvider(ClientMainEngine.PROVIDER)
class ScreenRenderer: BaseRenderer(), ILuaStaticDecl by LuaClientOnlyGenerated.ScreenRenderer_LuaGenerated {
    private var attachedScreen: Any? = null

    /* May dependent to Minecraft */

    @LuaFunction(name = "open")
    fun open() {
        if(attachedScreen != null)
            LogManager.getLogger().warn("Current screen already exists.")

        attachedScreen = object: Screen(Component.empty()) {
            override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
                if(minecraft?.level != null)
                    renderBackground(guiGraphics)
                else RenderUtil.renderer.loadMatrix(guiGraphics) {
                    fillRender(0, 0, width, height, 0, 0, 0, 0xff)
                }

                val mx = (i - (width - height * 16.0 / 9) / 2) * 1080 / height
                val my = j * 1080.0 / height

                RenderUtil.renderer.loadMatrix(guiGraphics) {
                    FHDScale(width, height) {
                        render(this, mx, my, f)
                    }
                }

                super.render(guiGraphics, i, j, f)
            }

            override fun mouseReleased(i: Double, j: Double, button: Int): Boolean {
                val mx = (i - (width - height * 16 / 9) / 2) * 1080 / height
                val my = j * 1080 / height
                addedWidgets.indices.forEach {
                    if(addedWidgets[it].clicked(mx, my, button))
                        return true
                }
                return false
            }

            override fun onClose() {
                super.onClose()
                attachedScreen = null
            }
        }
        Minecraft.getInstance().setScreen(attachedScreen as Screen)
    }

    @LuaFunction(name = "close")
    fun close() {
        if(Minecraft.getInstance().screen == attachedScreen)
            Minecraft.getInstance().setScreen(null)
        else LogManager.getLogger().warn("Current screen is not same with attached screen")
    }
}