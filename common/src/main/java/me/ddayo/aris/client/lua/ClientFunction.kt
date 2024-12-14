package me.ddayo.aris.client.lua

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.gui.BaseScreen
import me.ddayo.aris.client.gui.ImageTexture
import me.ddayo.aris.client.gui.TextureManager
import me.ddayo.aris.client.gui.element.ScriptClickableRenderer
import me.ddayo.aris.client.gui.element.ScriptDefaultTextRenderer
import me.ddayo.aris.client.gui.element.ScriptImageRenderer
import me.ddayo.aris.lua.math.Area
import me.ddayo.aris.lua.math.AreaBuilder
import me.ddayo.aris.lua.math.Point
import me.ddayo.aris.lua.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@LuaProvider(ClientFunction.CLIENT_ONLY)
@Environment(EnvType.CLIENT)
object ClientFunction {
    const val CLIENT_ONLY = "LuaClientOnlyGenerated"

    @LuaFunction(name = "create_area_builder")
    fun create() = AreaBuilder()

    @LuaFunction("create_point")
    fun create(x: Double, y: Double) = Point(x, y)

    @LuaFunction("create_window")
    fun createWindow() = BaseScreen()

    @LuaFunction("create_image_renderer")
    fun createRenderer(res: ImageTexture, x: Int, y: Int, width: Int, height: Int) = ScriptImageRenderer(
        res,
        x,
        y,
        width,
        height,
        Component.empty()
    )

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, area: Area) =
        ScriptClickableRenderer({ onClick.call() }, area, Component.empty())

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, x: Int, y: Int, width: Int, height: Int) =
        ScriptClickableRenderer(
            { onClick.call() },
            Area(x with y, x with (y + height), (x + width) with (y + height), (x + width) with y),
            Component.empty()
        )

    @LuaFunction("create_default_text_renderer")
    fun createDefaultTextRenderer(text: String, x: Int, y: Int, scale: Double, color: Int) =
        ScriptDefaultTextRenderer(text, Minecraft.getInstance().font, x, y, scale, color, Component.empty())

    @LuaFunction("load_image")
    fun loadImageRuntime(name: String, path: String): ImageTexture {
        return TextureManager.orLoadTexture(name) { ImageTexture(name, path) } as ImageTexture
    }

    @LuaFunction("close_screen")
    fun closeScreen() = Minecraft.getInstance().setScreen(null)
}