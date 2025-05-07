package me.ddayo.aris.client.gui

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import me.ddayo.aris.Aris
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.joml.Quaternionf
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33
import java.nio.ByteBuffer
import kotlin.math.atan2
import kotlin.math.sqrt


open class RenderUtilImpl : RenderUtil() {
    override fun readyRender() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
    }

    private fun bindTexture(idf: ResourceLocation) {
        // Minecraft.getInstance().textureManager.bindForSetup(idf)
        RenderSystem.setShaderTexture(0, idf)
    }

    override fun _useTexture(x: String) {
        bindTexture(ResourceLocation(Aris.MOD_ID, "textures/$x"))
    }

    override fun _useTexture(x: ImageResource) {
        x.bindTexture()
    }

    override fun unbindTexture() {
        bindTexture(ResourceLocation(Aris.MOD_ID, "textures/dummy.png"))
    }

    override fun resourceExists(x: String): Boolean {
        return Minecraft.getInstance().resourceManager.getResource(ResourceLocation(Aris.MOD_ID, "textures/$x")).isPresent
    }

    override fun _render(
        x: Double,
        y: Double,
        w: Double,
        h: Double,
        th1: Double,
        th2: Double,
        tv1: Double,
        tv2: Double
    ) = render(
        x.toFloat(),
        y.toFloat(),
        w.toFloat(),
        h.toFloat(),
        th1.toFloat(),
        th2.toFloat(),
        tv1.toFloat(),
        tv2.toFloat()
    )

    override fun fillRender(
        x: Double,
        y: Double,
        w: Double,
        h: Double,
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ) = fillRender(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), r, g, b, a)

    override fun fillRender(
        x: Double,
        y: Double,
        w: Double,
        h: Double,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) = fillRender(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), r, g, b, a)

    fun fillRender(x: Float, y: Float, w: Float, h: Float, r: Int, g: Int, b: Int, a: Int) {
        Tesselator.getInstance().apply {
            builder.apply {
                begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
                quads(x, y, w, h, r, g, b, a)
            }
            end()
        }
    }

    fun fillRender(x: Float, y: Float, w: Float, h: Float, r: Float, g: Float, b: Float, a: Float) {
        Tesselator.getInstance().apply {
            builder.apply {
                begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
                quadColor(x, y, w, h, r, g, b, a)
            }
            end()
        }
    }

    fun render(x: Float, y: Float, w: Float, h: Float, th1: Float, th2: Float, tv1: Float, tv2: Float) {
        Tesselator.getInstance().apply {
            builder.apply {
                begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
                quads(x, y, w, h, th1, th2, tv1, tv2)
            }
            end()
        }
    }

    override fun renderColorTex(x: Float, y: Float, w: Float, h: Float, r: Float, g: Float, b: Float, a: Float) {
        Tesselator.getInstance().apply {
            builder.apply {
                begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX)
                quadColorTex(x, y, w, h, 0.0f, 1.0f, 0.0f, 1.0f, r, g, b, a)
            }
            end()
        }
    }

    private fun BufferBuilder.quads(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        th1: Float,
        th2: Float,
        tv1: Float,
        tv2: Float
    ) {
        vertex(matrix.last().pose(), x, y + h, 0.0f).uv(th1, tv2).endVertex()
        vertex(matrix.last().pose(), x + w, y + h, 0.0f).uv(th2, tv2).endVertex()
        vertex(matrix.last().pose(), x + w, y, 0.0f).uv(th2, tv1).endVertex()
        vertex(matrix.last().pose(), x, y, 0.0f).uv(th1, tv1).endVertex()
    }

    private fun BufferBuilder.quadColorTex(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        th1: Float,
        th2: Float,
        tv1: Float,
        tv2: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        vertex(matrix.last().pose(), x, y + h, 0.0f).color(r, g, b, a).uv(th1, tv2).endVertex()
        vertex(matrix.last().pose(), x + w, y + h, 0.0f).color(r, g, b, a).uv(th2, tv2).endVertex()
        vertex(matrix.last().pose(), x + w, y, 0.0f).color(r, g, b, a).uv(th2, tv1).endVertex()
        vertex(matrix.last().pose(), x, y, 0.0f).color(r, g, b, a).uv(th1, tv1).endVertex()
    }

    private fun BufferBuilder.quads(x: Float, y: Float, w: Float, h: Float, r: Int, g: Int, b: Int, a: Int) {
        vertex(matrix.last().pose(), x, y + h, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x + w, y + h, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x + w, y, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x, y, 0.0f).color(r, g, b, a).endVertex()
    }

    private fun BufferBuilder.quadColor(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        vertex(matrix.last().pose(), x, y + h, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x + w, y + h, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x + w, y, 0.0f).color(r, g, b, a).endVertex()
        vertex(matrix.last().pose(), x, y, 0.0f).color(r, g, b, a).endVertex()
    }

    override fun getTexWidth() = GL33.glGetTexLevelParameteri(GL33.GL_TEXTURE_2D, 0, GL33.GL_TEXTURE_WIDTH)

    override fun getTexHeight() = GL33.glGetTexLevelParameteri(GL33.GL_TEXTURE_2D, 0, GL33.GL_TEXTURE_HEIGHT)

    override fun push() = matrix.pushPose()
    override fun pop() = matrix.popPose()

    override fun translate(x: Double, y: Double, z: Double) = matrix.translate(x, y, z)

    override fun rotate(x: Double, y: Double, z: Double) =
        matrix.mulPose(Quaternionf().rotationXYZ(x.toFloat(), y.toFloat(), z.toFloat()))

    override fun scale(x: Double, y: Double, z: Double) = matrix.scale(x.toFloat(), y.toFloat(), z.toFloat())

    override fun _useTexture(x: Int) {
        GlStateManager._bindTexture(x)
    }

    private var swizzleTest = false
    private fun swizzleDisabledGrayscaleBuffer(buf: ByteBuffer, width: Int, height: Int): ByteBuffer {
        val dir = BufferUtils.createByteBuffer(buf.remaining() * 4)
        while(buf.hasRemaining()){
            val d = buf.get()
            dir.put(d)
                .put(d)
                .put(d)
                .put(d)
        }
        dir.flip()
        return dir
    }

    override fun bindGrayscaleBuffer(buf: ByteBuffer, width: Int, height: Int) {
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_CLAMP_TO_EDGE)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_CLAMP_TO_EDGE)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR)

        swizzleTest = false
        if(!swizzleTest) {
            GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_R, GL33.GL_ALPHA)
            val gle = GL33.glGetError()
            if(gle != GL33.GL_NO_ERROR) { // test swizzle
                Minecraft.getInstance().player?.sendSystemMessage(Component.literal("GPU 드라이버 문제가 감지되어 느린 방법으로 우회합니다."))
                LogManager.getLogger().error("OpenGL error $gle: Cannot use auto swizzle with texture. On next try, it will uses cpu based method.")
                swizzleTest = true
            }
            else {
                GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_G, GL33.GL_ALPHA)
                GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_B, GL33.GL_ALPHA)
            }
        }
        if(!swizzleTest)
            GL33.glPixelStorei(GL33.GL_UNPACK_ALIGNMENT, 1)

        GL33.glTexImage2D(
            GL33.GL_TEXTURE_2D,
            0,
            GL33.GL_RGBA,
            width,
            height,
            0,
            if(swizzleTest) GL33.GL_RGBA else GL33.GL_ALPHA,
            GL33.GL_UNSIGNED_BYTE,
            if(swizzleTest) swizzleDisabledGrayscaleBuffer(buf, width, height) else buf
        )
    }
}

abstract class RenderUtil {
    companion object {
        val renderer: RenderUtil = RenderUtilImpl()
    }

    fun useTexture(x: String, f: () -> Unit) {
        _useTexture(x)
        f()
        unbindTexture()
    }

    fun useTexture(x: ImageResource, f: () -> Unit) {
        _useTexture(x)
        f()
        unbindTexture()
    }

    fun useTexture(x: Int, f: () -> Unit) {
        _useTexture(x)
        f()
        unbindTexture()
    }

    open fun bindGrayscaleBuffer(buf: ByteBuffer, width: Int, height: Int) {
        throw NotImplementedError("bindGrayscaleBuffer(): Int not implemented")
    }

    protected abstract fun _useTexture(x: String)
    protected abstract fun _useTexture(x: ImageResource)
    protected open fun _useTexture(x: Int) {
        throw NotImplementedError("_useTexture(Int) not implemented")
    }

    protected abstract fun unbindTexture()

    abstract fun resourceExists(x: String): Boolean
    fun resourceExists(x: ImageResource): Boolean {
        return false
    }

    fun _render() = _render(0, 0, 1920, 1080)

    fun fillRender(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int, a: Int) =
        fillRender(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble(), r, g, b, a)

    fun fillRender(x: Int, y: Int, w: Int, h: Int, r: Double, g: Double, b: Double, a: Double) = fillRender(
        x.toDouble(),
        y.toDouble(),
        w.toDouble(),
        h.toDouble(),
        r.toFloat(),
        g.toFloat(),
        b.toFloat(),
        a.toFloat()
    )

    fun fillRender(x: Double, y: Double, w: Double, h: Double, r: Double, g: Double, b: Double, a: Double) =
        fillRender(x, y, w, h, r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())

    fun _render(x: Int, y: Int, w: Int, h: Int) = _render(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

    fun _render(x: Double, y: Double, w: Double, h: Double) = _render(x, y, w, h, 0.0, 1.0, 0.0, 1.0)
    fun renderColorTex(x: Double, y: Double, w: Double, h: Double, r: Double, g: Double, b: Double, a: Double) =
        renderColorTex(
            x.toFloat(),
            y.toFloat(),
            w.toFloat(),
            h.toFloat(),
            r.toFloat(),
            g.toFloat(),
            b.toFloat(),
            a.toFloat()
        )

    abstract fun fillRender(x: Double, y: Double, w: Double, h: Double, r: Int, g: Int, b: Int, a: Int)
    abstract fun fillRender(x: Double, y: Double, w: Double, h: Double, r: Float, g: Float, b: Float, a: Float)
    abstract fun _render(x: Double, y: Double, w: Double, h: Double, th1: Double, th2: Double, tv1: Double, tv2: Double)
    abstract fun renderColorTex(x: Float, y: Float, w: Float, h: Float, r: Float, g: Float, b: Float, a: Float)

    abstract fun getTexWidth(): Int
    abstract fun getTexHeight(): Int

    abstract fun push()
    abstract fun pop()
    fun push(f: () -> Unit) {
        push()
        f()
        pop()
    }

    abstract fun translate(x: Double, y: Double, z: Double)
    abstract fun rotate(x: Double, y: Double, z: Double)
    abstract fun scale(x: Double, y: Double, z: Double)
    open fun readyRender() {}

    fun fixScale(width: Int, height: Int, newWidth: Int, newHeight: Int, x: () -> Unit) = fixScale(width.toDouble(), height.toDouble(), newWidth.toDouble(), newHeight.toDouble(), x)

    fun fixScale(width: Double, height: Double, newWidth: Double, newHeight: Double, x: () -> Unit) {
        push {
            translate((width - height * newWidth / newHeight) / 2, 0.0, 0.0)
            scale(height / newHeight, height / newHeight, height / newHeight)
            x()
        }
    }

    lateinit var matrix: PoseStack
    lateinit var graphics: GuiGraphics

    fun loadMatrix(graphics: GuiGraphics, f: RenderUtil.() -> Unit) {
        this.matrix = graphics.pose()
        this.graphics = graphics
        this.f()
    }

    fun drawLine(sx: Double, sy: Double, ex: Double, ey: Double, d: Double, r: Int, g: Int, b: Int, a: Int = 255) {
        push {
            val dx = ex - sx
            val dy = ey - sy
            translate(sx, sy, 0.0)
            rotate(0.0, 0.0, Math.toDegrees(atan2(dy, dx)))
            fillRender(0.0, -d, sqrt(dx * dx + dy * dy), d * 2, r, g, b, a)
        }
    }
}
