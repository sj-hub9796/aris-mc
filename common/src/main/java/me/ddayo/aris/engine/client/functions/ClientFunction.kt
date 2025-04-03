package me.ddayo.aris.engine.client.functions

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.gui.BaseComponent
import me.ddayo.aris.client.gui.BaseRectComponent
import me.ddayo.aris.client.gui.ImageResource
import me.ddayo.aris.math.Area
import me.ddayo.aris.math.AreaBuilder
import me.ddayo.aris.math.Point
import me.ddayo.aris.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.client.gui.ScreenRenderer
import me.ddayo.aris.client.gui.element.*
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.client.ClientMainEngine
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft

@LuaProvider(ClientMainEngine.PROVIDER, library = "aris.client")
@Environment(EnvType.CLIENT)
object ClientFunction {
    @LuaFunction(name = "create_area_builder")
    fun create() = AreaBuilder()

    @LuaFunction("create_point")
    fun create(x: Double, y: Double) = Point(x, y)

    @LuaFunction("create_window")
    fun createWindow() = ScreenRenderer()

    @LuaFunction("create_image_renderer")
    fun createImageRenderer(res: ImageResource) = ScriptImageRenderer(res)

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, area: Area) =
        ScriptClickableRenderer({ onClick.call() }, area)

    @LuaFunction("create_clickable")
    fun createClickable(onClick: LuaFunc, x: Int, y: Int, width: Int, height: Int) =
        ScriptClickableRenderer(
            onClick::call,
            Area(x with y, x with (y + height), (x + width) with (y + height), (x + width) with y)
        )

    @LuaFunction("create_color_renderer")
    fun createColorRenderer(r: Int, g: Int, b: Int, a: Int) = ScriptColorRenderer(r, g, b, a)

    @LuaFunction("create_color_renderer")
    fun createColorRenderer(color: Long) = ScriptColorRenderer(color)

    @LuaFunction("create_default_text_renderer")
    fun createDefaultTextRenderer(text: String, color: Int) =
        ScriptDefaultTextRenderer(text, Minecraft.getInstance().font, color)

    @LuaFunction("create_item_renderer")
    fun createItemRenderer(item: LuaItemStack) = ScriptItemRenderer(item.inner)

    @LuaFunction("load_image")
    fun loadImageRuntime(path: String): ImageResource = ImageResource.getOrCreate(path)

    @LuaFunction("load_image")
    fun loadImageRuntime(name: String, path: String): ImageResource {
        return ImageResource.getOrCreate(path)
    }

    @LuaFunction("create_component")
    fun createComponent() = BaseComponent()

    @LuaFunction("create_rect_component")
    fun createRectComponent() = BaseRectComponent()

    @LuaFunction("close_screen")
    fun closeScreen() = Minecraft.getInstance().setScreen(null)
}