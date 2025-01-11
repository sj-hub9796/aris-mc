package me.ddayo.aris.client.engine.functions

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.ImageResource
import me.ddayo.aris.client.gui.element.ScriptClickableRenderer
import me.ddayo.aris.client.gui.element.ScriptDefaultTextRenderer
import me.ddayo.aris.client.gui.element.ScriptImageRenderer
import me.ddayo.aris.math.Area
import me.ddayo.aris.math.AreaBuilder
import me.ddayo.aris.math.Point
import me.ddayo.aris.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.client.gui.ScreenRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@LuaProvider(ClientMainEngine.PROVIDER)
@Environment(EnvType.CLIENT)
object ClientFunction {

    @LuaFunction(name = "create_area_builder")
    fun create() = AreaBuilder()

    @LuaFunction("create_point")
    fun create(x: Double, y: Double) = Point(x, y)

    @LuaFunction("create_window")
    fun createWindow() = ScreenRenderer()

    @LuaFunction("create_image_renderer")
    fun createRenderer(res: ImageResource, x: Int, y: Int, width: Int, height: Int) = ScriptImageRenderer(
        res,
        x,
        y,
        width,
        height
    )

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, area: Area) =
        ScriptClickableRenderer({ onClick.call() }, area)

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, x: Int, y: Int, width: Int, height: Int) =
        ScriptClickableRenderer(
            { onClick.call() },
            Area(x with y, x with (y + height), (x + width) with (y + height), (x + width) with y)
        )

    @LuaFunction("create_default_text_renderer")
    fun createDefaultTextRenderer(text: String, x: Int, y: Int, scale: Double, color: Int) =
        ScriptDefaultTextRenderer(text, Minecraft.getInstance().font, x, y, scale, color)

    @LuaFunction("load_image")
    fun loadImageRuntime(path: String): ImageResource = ImageResource.getOrCreate(path)

    @LuaFunction("load_image")
    fun loadImageRuntime(name: String, path: String): ImageResource {
        return ImageResource.getOrCreate(path)
    }

    @LuaFunction("close_screen")
    fun closeScreen() = Minecraft.getInstance().setScreen(null)
}