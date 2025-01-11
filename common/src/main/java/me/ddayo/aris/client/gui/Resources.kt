package me.ddayo.aris.client.gui

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import me.ddayo.aris.Aris
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype
import java.io.File
import java.net.URL
import java.nio.ByteBuffer
import kotlin.math.roundToInt


abstract class Resource(private val uri: String) {
    enum class ResourceType {
        Images,
        Videos,
        Fonts,
        Scripts,
        Sounds,
        Misc
    }

    abstract val resourceType: ResourceType

    private fun loadTester() {
        LogManager.getLogger().info(uri)
        if (uri.startsWith("jar:")) {
            finishLoad(
                javaClass.getResourceAsStream(
                    "assets/${resourceType.name.lowercase()}/${
                        uri.substring(
                            4
                        )
                    }"
                )!!.readBytes()
            )
            return
        }
        if (uri.startsWith("http:") || uri.startsWith("https:")) {
            finishLoad(URL(uri).readBytes())
            LogManager.getLogger().info("Network resource loaded!")
            return
        }
        val t = File("assets/${resourceType.name.lowercase()}", uri).let {
            if (it.exists())
                it.readBytes()
            else null
        }
        if (t != null) {
            finishLoad(t)
            return
        }
        throw IllegalArgumentException("Not able to load resource: $uri")
    }

    lateinit var thrown: Throwable
        private set
    protected lateinit var bytes: ByteArray
        private set

    fun suspend(): ByteArray {
        loader.join()
        if (::bytes.isInitialized) return bytes
        else throw thrown
    }

    protected open fun afterLoad() {}

    fun getOrNull() = if (::bytes.isInitialized) bytes
    else if (::thrown.isInitialized) throw thrown
    else null

    private val loader = Thread {
        try {
            loadTester()
            LogManager.getLogger().info("Resource $uri loaded.")
        } catch (e: Throwable) {
            thrown = e
            LogManager.getLogger().error(e.message)
            LogManager.getLogger().error(e.stackTraceToString())
        }
    }

    var isLoaded = false
        private set

    private fun finishLoad(b: ByteArray) {
        Minecraft.getInstance().execute {
            bytes = b
            isLoaded = true
            afterLoad()
        }
    }

    protected fun startLoading() {
        loader.start()
    }

    companion object {
        fun clearResource() {
            ImageResource.clearResource()
            FontResource.clearResource()
            Minecraft.getInstance().player?.sendSystemMessage(Component.literal("Custom script resource reloaded!!"))
        }
    }
}

class FontResource(uri: String) : Resource(uri) {
    class STBFontInstance(private val buf: ByteBuffer) : NativeInstance<STBTTFontinfo>() {
        override lateinit var nativeInstance: STBTTFontinfo

        override fun allocateInternal() {
            nativeInstance = STBTTFontinfo.create()
            STBTruetype.stbtt_InitFont(nativeInstance, buf)
        }

        override fun freeInternal() {
            nativeInstance.free()
        }
    }

    companion object {
        private var currentId = 0
        private val resourceMap = mutableMapOf<String, FontResource>()
        fun getOrCreate(uri: String) = resourceMap.getOrPut(uri) { FontResource(uri) }
        fun free(uri: String) = resourceMap[uri]?.info?.free()?.also { resourceMap.remove(uri) }

        fun clearResource() {
            resourceMap.forEach { it.value.info.free() }
            resourceMap.clear()
        }
    }

    private val cid = currentId++

    override val resourceType: ResourceType
        get() = ResourceType.Fonts

    lateinit var info: STBFontInstance
    override fun afterLoad() {
        val byteBuf = BufferUtils.createByteBuffer(bytes.size)
        byteBuf.put(bytes)
        byteBuf.flip()
        info = STBFontInstance(byteBuf)
    }

    init {
        startLoading()
    }

    private var _ascent = createIntArray()
    private var _descent = createIntArray()
    private var _lineGap = createIntArray()

    public val ascent: Int
        get() = this._ascent[0]
    public val descent: Int
        get() = this._descent[0]

    public fun getScale(height: Int): Float {
        info.claim {
            return STBTruetype.stbtt_ScaleForMappingEmToPixels(it, height.toFloat())
        }
    }

    fun calculateWidth(text: String, height: Int): Int {
        info.claim { fontInfo ->
            val scale = STBTruetype.stbtt_ScaleForMappingEmToPixels(fontInfo, height.toFloat())
            var x = 0
            for (k in text.withIndex()) {
                val ax = createIntArray()
                val lsb = createIntArray()
                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, k.value.code, ax, lsb)

                x += (ax[0] * scale).roundToInt()
                x += if (k.index != text.length - 1)
                    (STBTruetype.stbtt_GetCodepointKernAdvance(
                        fontInfo,
                        k.value.code,
                        text[k.index + 1].code
                    ) * scale).roundToInt()
                else (STBTruetype.stbtt_GetCodepointKernAdvance(fontInfo, k.value.code, 0) * scale).roundToInt()
            }

            return x
        }
    }

    fun getBitmap(text: String, height: Int): ByteBuffer {
        info.claim { fontInfo ->
            val width = calculateWidth(text, height)
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, _ascent, _descent, _lineGap)

            val buf = BufferUtils.createByteBuffer(width * height * 2)
            val scale = STBTruetype.stbtt_ScaleForMappingEmToPixels(fontInfo, height.toFloat())

            _ascent[0] = (ascent * scale).roundToInt()
            _descent[0] = (descent * scale).roundToInt()

            var x = 0
            for (k in text.withIndex()) {
                val ax = createIntArray()
                val lsb = createIntArray()
                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, k.value.code, ax, lsb)

                val c_x1 = createIntArray()
                val c_x2 = createIntArray()
                val c_y1 = createIntArray()
                val c_y2 = createIntArray()

                STBTruetype.stbtt_GetCodepointBitmapBox(fontInfo, k.value.code, scale, scale, c_x1, c_y1, c_x2, c_y2)

                val y = ascent + c_y1[0]
                buf.position(x + (lsb[0] * scale).roundToInt() + (y * width))
                STBTruetype.stbtt_MakeCodepointBitmap(
                    fontInfo,
                    buf,
                    c_x2[0] - c_x1[0],
                    c_y2[0] - c_y1[0],
                    width,
                    scale,
                    scale,
                    k.value.code
                )

                if (k.index != text.length - 1)
                    x += (ax[0] * scale).roundToInt() + (STBTruetype.stbtt_GetCodepointKernAdvance(
                        fontInfo,
                        k.value.code,
                        text[k.index + 1].code
                    ) * scale).roundToInt()
            }
            buf.position(width * height * 2)
            buf.flip()
            return buf
        }
    }

    private fun createIntArray() = arrayOf(0).toIntArray()
}

class ImageResource(uri: String) : Resource(uri) {
    companion object {
        private var currentId = 0
        fun getNewIdentifier() = ResourceLocation(Aris.MOD_ID, "private/images/${currentId++}")
        private val resourceMap = mutableMapOf<String, ImageResource>()
        fun getOrCreate(uri: String) = resourceMap.getOrPut(uri) { ImageResource(uri) }

        fun clearResource() {
            val tm = Minecraft.getInstance().textureManager
            resourceMap.forEach {
                if (it.value::rl.isInitialized && tm.getTexture(it.value.rl) != null)
                    tm.release(it.value.rl)
            }
        }
    }

    override val resourceType: ResourceType
        get() = ResourceType.Images
    private lateinit var rl: ResourceLocation

    fun loadAsTexture(): ResourceLocation? {
        if (::rl.isInitialized) return rl
        return getOrNull()?.let {
            val dbuf = ByteBuffer.allocateDirect(it.size)
            dbuf.put(it)
            dbuf.flip()
            rl = getNewIdentifier()

            Minecraft.getInstance().textureManager.register(rl, DynamicTexture(NativeImage.read(dbuf)))
            rl
        }
    }

    fun bindTexture() {
        loadAsTexture()?.let {
            // Minecraft.getInstance().textureManager.bindForSetup(it)
            RenderSystem.setShaderTexture(0, it)
        }
    }

    init {
        startLoading()
    }
}
