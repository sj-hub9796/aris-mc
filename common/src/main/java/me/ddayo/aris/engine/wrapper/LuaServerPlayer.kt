package me.ddayo.aris.engine.wrapper

import me.ddayo.aris.Aris
import me.ddayo.aris.luagen.CoroutineProvider
import me.ddayo.aris.luagen.ILuaStaticDecl
import me.ddayo.aris.luagen.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.lua.glue.InGameGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProperty
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand


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

    @LuaFunction(name = "add_effect")
    fun addEffect(effect: LuaMobEffectInstance) {
        player.addEffect(effect.build())
    }

    @LuaFunction(name = "clear_effect")
    fun clearEffect() {
        player.removeAllEffects()
    }

    @LuaFunction(name = "remove_effect")
    fun removeEffect(of: String) {
        player.removeEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation(of))!!)
    }

    @LuaFunction(name = "remove_effect")
    fun removeEffect(ns: String, of: String) {
        player.removeEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation(ns, of))!!)
    }
}