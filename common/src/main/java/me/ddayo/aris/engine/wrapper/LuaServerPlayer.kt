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
import kotlin.math.cos
import kotlin.math.sin


@LuaProvider(InGameEngine.PROVIDER, library = "aris.game")
object LuaServerPlayerFunctions : CoroutineProvider {
    @LuaFunction(name = "iter_players")
            /**
             * 모든 플레이어를 한번씩 callback으로 넘겨줍니다.
             * 플레이어 리스트에서 for문을 돌리는 것과 유사합니다.
             * @param fn callback
             */
    fun iterPlayers(fn: LuaFunc) = coroutine<Unit> {
        Aris.server.playerList.players.forEach {
            fn.await(this@coroutine, LuaServerPlayer(it))
        }
    }
}

@LuaProvider(InGameEngine.PROVIDER)
class LuaServerPlayer(player: ServerPlayer) : LuaPlayerEntity(player), CoroutineProvider,
    ILuaStaticDecl by InGameGenerated.LuaServerPlayer_LuaGenerated {

    @LuaProperty("main_hand_item")
    /**
     * 오른손에 들고있는 아이템을 가져오거나 설정할 수 있습니다.
     */
    override var mainHandItem
        get() = super.mainHandItem
        set(value) {
            player.setItemInHand(InteractionHand.MAIN_HAND, value.inner)
        }

    /**
     * 플레이어를 특정 상대적인 위치로 텔레포트 시킵니다.
     * @param x 이동시킬 x좌표의 상대적인 값
     * @param y 이동시킬 y좌표의 상대적인 값
     * @param z 이동시킬 z좌표의 상대적인 값
     */
    @LuaFunction("move_delta")
    fun moveDelta(x: Double, y: Double, z: Double) {
        player.teleportTo(player.x + x, player.y + y, player.z + z)
    }

    /**
     * 플레이어를 특정 위치로 텔레포트 시킵니다.
     * @param x 이동시킬 x좌표
     * @param y 이동시킬 y좌표
     * @param z 이동시킬 z좌표
     */
    @LuaFunction("move_to")
    fun moveTo(x: Double, y: Double, z: Double) {
        player.teleportTo(x, y, z)
    }

    /**
     * 플레이어를 바라보는 위치를 기준으로 하는 상대적인 위치로 텔레포트 시킵니다.
     * @param x 앞으로 이동할 칸수
     * @param y 위로 이동할 칸수
     * @param z 옆으로 이동할 칸수(+는 오른쪽을 의미)
     */
    @LuaFunction("move_delta_relative")
    fun moveDeltaRelative(x: Double, y: Double, z: Double) {
        val yawRad = Math.toRadians(player.yRot.toDouble())

        // Forward vector (XZ plane only, ignoring pitch)
        val forwardX = -sin(yawRad)
        val forwardZ = cos(yawRad)

        // Right vector (perpendicular to forward vector)
        val rightX = cos(yawRad)
        val rightZ = sin(yawRad)

        // Calculate the new position
        val newX = player.x + forwardX * x + rightX * z
        val newY = player.y + y
        val newZ = player.z + forwardZ * x + rightZ * z
        moveTo(newX, newY, newZ)
    }

    /**
     * 채팅으로 텍스트 메시지를 전송
     * @param msg 전송할 텍스트
     */
    @LuaFunction("send_message_text")
    fun sendMessage(msg: String) = sendMessage(Component.literal(msg))

    @LuaFunction("send_message")
    fun sendMessage(msg: Component) {
        player.sendSystemMessage(msg)
    }

    /**
     * 플레이어로부터 유클리드 거리(직선거리) 기준 특정 거리 이내인 플레이어를 탐색합니다.
     * @param fn callback
     * @param lnt 플레이어로부터의 거리
     * @param includeSelf true인 경우 자기 자신을 포함하고, false인 경우 제외합니다.
     * @see aris.game.iter_players
     */
    @LuaFunction(name = "iter_player_nearby")
    fun iterPlayers(fn: LuaFunc, lnt: Double, includeSelf: Boolean) = coroutine<Unit> {
        Aris.server.playerList.players.forEach {
            if (it.position().distanceTo(player.position()) < lnt && (includeSelf || it != player))
                fn.await(this@coroutine, LuaServerPlayer(it))
        }
    }
}