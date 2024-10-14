package me.ddayo.aris.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix4f

object RenderUtil {
    fun setTexture(resource: ResourceLocation, ix: Int = 0) {
        RenderSystem.setShaderTexture(ix, resource)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
    }

    inline fun PoseStack.push(f: () -> Unit) {
        pushPose()
        f()
        popPose()
    }

    fun render(matrix4f: Matrix4f, x: Int, y: Int, width: Int, height: Int)
            = render(matrix4f, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

    fun render(matrix4f: Matrix4f, x: Double, y: Double, width: Double, height: Double)
            = render(matrix4f, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

    fun render(matrix4f: Matrix4f, x: Float, y: Float, width: Float, height: Float) {
        val bufferBuilder = Tesselator.getInstance().builder
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        bufferBuilder.vertex(matrix4f, x, y, 0f).uv(0f, 0f).endVertex()
        bufferBuilder.vertex(matrix4f, x, (y + height), 0f).uv(0f, 1f).endVertex()
        bufferBuilder.vertex(matrix4f, (x + width), (y + height), 0f).uv(1f, 1f).endVertex()
        bufferBuilder.vertex(matrix4f, (x + width), y, 0f).uv(1f, 0f).endVertex()
        BufferUploader.drawWithShader(bufferBuilder.end())
    }
}