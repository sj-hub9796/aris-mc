package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity

@LuaProvider(InGameEngine.PROVIDER)
open class LuaEntity(val inner: Entity) : ILuaStaticDecl by InGameGenerated.LuaEntity_LuaGenerated {
    /**
     * 엔티티의 이름을 가져옵니다.
     */
    @LuaProperty("name")
    val name get() = inner.name.string

    /**
     * 엔티티의 표시된 이름을 가져옵니다.
     */
    @LuaProperty("display_name")
    val displayName get() = inner.displayName.string

    /**
     * 엔티티의 커스텀 이름을 설정하거나 가져올 수 있습니다.
     */
    @LuaProperty("custom_name")
    var customName
        get() = inner.customName?.string ?: "None"
        set(value) {
            inner.customName = Component.literal(value)
        }

    /**
     * 플레이어의 X좌표를 가져오거나 설정할 수 있습니다.
     */
    @LuaProperty
    var x
        get() = inner.x
        set(value) {
            inner.teleportTo(value, inner.y, inner.z)
        }

    /**
     * 엔티티의 Y좌표를 가져오거나 설정할 수 있습니다.
     */
    @LuaProperty
    var y
        get() = inner.y
        set(value) {
            inner.teleportTo(inner.x, value, inner.z)
        }

    /**
     * 엔티티의 Z좌표를 가져오거나 설정할 수 있습니다.
     */
    @LuaProperty
    var z
        get() = inner.z
        set(value) {
            inner.teleportTo(inner.x, inner.y, value)
        }

    /**
     * 엔티티에 특정 데미지를 줄 수 있습니다.
     */
    @LuaFunction(name = "add_damage")
    fun addDamage(damage: Double) {
        inner.hurt(inner.damageSources().fellOutOfWorld(), damage.toFloat())
    }
}