package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.wrapper.LuaServerPlayerFunctions.coroutine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import kotlin.math.cos
import kotlin.math.sin


@LuaProvider(InGameEngine.PROVIDER)
open class LuaEntity(val inner: Entity) : ILuaStaticDecl by InGameGenerated.LuaEntity_LuaGenerated {
    /**
     * 엔티티의 이름을 가져옵니다.
     */
    @LuaProperty("name")
    val name get() = inner.name.string

    @LuaProperty
    val type get() = BuiltInRegistries.ENTITY_TYPE.getKey(inner.type).toString()

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

    @LuaFunction("add_velocity")
    fun addVelocity(x: Double, y: Double, z: Double) {
        inner.let {
            it.setDeltaMovement(x, y, z)
            it.hurtMarked = true
            it.hasImpulse = true
        }
    }

    @LuaFunction("add_velocity_relative")
    fun addVelocityRelative(x: Double, y: Double, z: Double) {
        val yawRad = Math.toRadians(inner.yRot.toDouble())

        // Forward vector (XZ plane only, ignoring pitch)
        val forwardX = -sin(yawRad)
        val forwardZ = cos(yawRad)

        // Right vector (perpendicular to forward vector)
        val rightX = cos(yawRad)
        val rightZ = sin(yawRad)

        // Calculate the new position
        val newX = forwardX * x + rightX * z
        val newY = y
        val newZ = forwardZ * x + rightZ * z
        addVelocity(newX, newY, newZ)
    }

    /**
     * 엔티티를 특정 상대적인 위치로 텔레포트 시킵니다.
     * @param x 이동시킬 x좌표의 상대적인 값
     * @param y 이동시킬 y좌표의 상대적인 값
     * @param z 이동시킬 z좌표의 상대적인 값
     */
    @LuaFunction("move_delta")
    fun moveDelta(x: Double, y: Double, z: Double) {
        inner.teleportTo(inner.x + x, inner.y + y, inner.z + z)
    }

    /**
     * 엔티티를 특정 위치로 텔레포트 시킵니다.
     * @param x 이동시킬 x좌표
     * @param y 이동시킬 y좌표
     * @param z 이동시킬 z좌표
     */
    @LuaFunction("move_to")
    fun moveTo(x: Double, y: Double, z: Double) {
        inner.teleportTo(x, y, z)
    }

    /**
     * 엔티티를 바라보는 위치를 기준으로 하는 상대적인 위치로 텔레포트 시킵니다.
     * @param x 앞으로 이동할 칸수
     * @param y 위로 이동할 칸수
     * @param z 옆으로 이동할 칸수(+는 오른쪽을 의미)
     */
    @LuaFunction("move_delta_relative")
    fun moveDeltaRelative(x: Double, y: Double, z: Double) {
        val yawRad = Math.toRadians(inner.yRot.toDouble())

        // Forward vector (XZ plane only, ignoring pitch)
        val forwardX = -sin(yawRad)
        val forwardZ = cos(yawRad)

        // Right vector (perpendicular to forward vector)
        val rightX = cos(yawRad)
        val rightZ = sin(yawRad)

        // Calculate the new position
        val newX = inner.x + forwardX * x + rightX * z
        val newY = inner.y + y
        val newZ = inner.z + forwardZ * x + rightZ * z
        moveTo(newX, newY, newZ)
    }

    @LuaFunction("iter_entities_nearby")
    fun getEntitiesNearby(fn: LuaFunc, radius: Double, includeSelf: Boolean) = coroutine<Unit> {
        val level = inner.level()
        val area: AABB? = inner.boundingBox.inflate(radius)
        level.getEntities(inner, area) {
            if(includeSelf || it != inner)
            fn.await(this, LuaEntity(it))
            true
        }
    }
}