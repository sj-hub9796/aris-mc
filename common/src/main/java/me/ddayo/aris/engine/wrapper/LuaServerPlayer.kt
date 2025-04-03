package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.Aris
import me.ddayo.aris.CoroutineProvider
import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.phys.Vec3
import org.apache.logging.log4j.LogManager


@LuaProvider(InGameEngine.PROVIDER, library = "aris")
object LuaServerPlayerFunctions: CoroutineProvider {
    @LuaFunction(name = "iter_players")
    fun iterPlayers(fn: LuaFunc) = coroutine<Unit> {
        Aris.server.playerList.players.forEach {
            fn.await(this@coroutine, LuaServerPlayer(it))
        }
    }

    @LuaFunction(name = "add_damage")
    fun damagePlayer(player: LuaEntity, damage: Double) {
        player.inner.hurt(player.inner.damageSources().fellOutOfWorld(), damage.toFloat())
    }
}

@LuaProvider(InGameEngine.PROVIDER)
class LuaServerPlayer(player: ServerPlayer) : LuaPlayerEntity(player), CoroutineProvider,
    ILuaStaticDecl by InGameGenerated.LuaServerPlayer_LuaGenerated {

    @LuaProperty("main_hand_item")
    override var mainHandItem
        get() = super.mainHandItem
        set(value) {
            player.setItemInHand(InteractionHand.MAIN_HAND, value.inner)
        }

    @LuaProperty
    override var x
        get() = inner.x
        set(value) {
            player.moveTo(value, player.y, player.z)
        }

    @LuaProperty
    override var y
        get() = inner.y
        set(value) {
            player.moveTo(player.x, value, player.z)
        }

    @LuaProperty
    override var z
        get() = inner.z
        set(value) {
            player.moveTo(player.x, player.y, value)
        }

    @LuaFunction("move_delta")
    fun moveDelta(x: Double, y: Double, z: Double) {
        player.moveTo(player.x + x, player.y + y, player.z + z)
    }

    @LuaFunction("move_to")
    fun moveTo(x: Double, y: Double, z: Double) {
        player.moveTo(x, y, z)
    }

    @LuaFunction("send_message_text")
    fun sendMessage(msg: String) = sendMessage(Component.literal(msg))

    @LuaFunction("send_message")
    fun sendMessage(msg: Component) {
        player.sendSystemMessage(msg)
    }

    @LuaFunction(name = "iter_player_nearby")
    fun iterPlayers(fn: LuaFunc, lnt: Double) = coroutine<Unit> {
        Aris.server.playerList.players.forEach {
            if(it.position().distanceTo(player.position()) < lnt)
                fn.await(this@coroutine, LuaServerPlayer(it))
        }
    }
}