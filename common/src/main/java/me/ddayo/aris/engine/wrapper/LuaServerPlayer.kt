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
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.Vec3


@LuaProvider(InGameEngine.PROVIDER)
object LuaServerPlayerFunctions: CoroutineProvider {
    @LuaFunction(name = "iter_players")
    fun iterPlayers(fn: LuaFunc) = coroutine<Unit> {
        Aris.server.playerList.players.forEach {
            fn.await(this@coroutine, LuaServerPlayer(it))
        }
    }
}

@LuaProvider(InGameEngine.PROVIDER)
class LuaServerPlayer(private val player: ServerPlayer) : LuaPlayerEntity(player),
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
}