package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance


@LuaProvider(InGameEngine.PROVIDER)
class LuaMobEffectInstance(val rl: ResourceLocation): ILuaStaticDecl by InGameGenerated.LuaMobEffectInstance_LuaGenerated {
    /**
     * Duration(tick)
     */
    @LuaProperty
    var duration = 20
    @LuaProperty
    var amplifier = 0

    /**
     * 거품 표시 여부
     */
    @LuaProperty
    var ambient = false
    @LuaProperty
    var visible = false
    @LuaProperty
    var showIcon = false

    fun build() = MobEffectInstance(
        BuiltInRegistries.MOB_EFFECT.get(rl)!!,
        duration, amplifier, ambient, visible, showIcon
    )
}