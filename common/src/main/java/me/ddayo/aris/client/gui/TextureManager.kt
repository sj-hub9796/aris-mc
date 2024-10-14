package me.ddayo.aris.client.gui

import com.mojang.blaze3d.platform.NativeImage
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL


object TextureManager {
    val loadedTextures = mutableMapOf<String, Texture>()
    inline fun orLoadTexture(str: String, f: () -> Texture) = loadedTextures.getOrPut(str, f)
}

abstract class Texture {

}

@LuaProvider
class ImageTexture(val name: String, uri: String) : Texture() {
    private var bytes: InputStream? = null

    init {
        Thread {
            if (uri.startsWith("https://") || uri.startsWith("http://"))
                bytes = URL(uri).openStream()
            else if (uri.startsWith("jar://"))
                TODO()
            else bytes = File(uri).inputStream()
            LogManager.getLogger().error("Loaded")
        }.start()
    }

    var location: ResourceLocation? = null
        private set

    private var loaded = false

    fun tryBind() {
        if (!loaded)
            bytes?.let {
                LogManager.getLogger().info("Register")
                location = Minecraft.getInstance().textureManager.register(name, DynamicTexture(NativeImage.read(it)))
                loaded = true
            }
    }

    @LuaFunction
    fun dummy() {
    }
}