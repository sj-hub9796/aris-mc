package me.ddayo.aris.engine

import me.ddayo.aris.Aris
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

@LuaProvider(InitEngine.PROVIDER)
object InitFunction {
    @LuaFunction
    fun dummy() {}

    @LuaFunction("create_item")
    fun registerItem(key: String) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation(Aris.MOD_ID, key), Item(Item.Properties()))
    }
}