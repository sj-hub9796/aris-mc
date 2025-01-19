package me.ddayo.aris.client.gui.element

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.client.gui.BaseComponent
import me.ddayo.aris.client.gui.RenderUtil
import me.ddayo.aris.engine.LuaItemStack
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

@LuaProvider(ClientMainEngine.PROVIDER)
class ScriptItemRenderer(
    private var item: ItemStack
) : BaseComponent(),
    ILuaStaticDecl by LuaClientOnlyGenerated.ScriptItemRenderer_LuaGenerated {
    companion object {
        private const val ITEM_SIZE = 16
    }

    override val isScaleRateFixed = true

    @LuaProperty("item")
    var luaItem
        get() = LuaItemStack(item)
        set(value) {
            item = value.inner
        }

    override fun RenderUtil._render(mx: Double, my: Double, delta: Float) {
        this.graphics.renderItem(if (item.isEmpty) Items.BARRIER.defaultInstance else item, 0, 0)
    }
}