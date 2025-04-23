package me.ddayo.aris.engine.client.functions

import me.ddayo.aris.LuaFunc
import me.ddayo.aris.client.gui.HudRenderer
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.networking.PacketDeclaration
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.luagen.RetrieveEngine
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack


@LuaProvider(ClientInGameEngine.PROVIDER, library = "aris.game.client")
object ClientInGameFunction {
    private val mc by lazy { Minecraft.getInstance() }
    /**
     * 채팅창에 새로운 텍스트를 추가합니다.
     * @param message 추가할 메시지
     */
    @LuaFunction("send_system_message")
    fun sendSystemMessage(message: String) {
        mc.player!!.sendSystemMessage(Component.literal(message))
    }

    /**
     * 채팅창에 /command를 입력하는 것과 동일합니다.
     * @param command 실행할 커멘드
     */
    @LuaFunction("invoke_command")
    fun invokeCommand(command: String) {
        mc.player!!.connection.sendCommand(command)
    }

    /**
     * 현재 플레이어의 x좌표를 구합니다.
     */
    @LuaFunction("get_player_x")
    fun getPlayerX() = mc.player!!.x

    /**
     * 현재 플레이어의 y좌표를 구합니다.
     */
    @LuaFunction("get_player_y")
    fun getPlayerY() = mc.player!!.y

    /**
     * 매 틱마다 실행할 함수를 추가합니다.
     * @param f 실행할 함수
     */
    @LuaFunction("add_tick_hook")
    fun addTickHook(@RetrieveEngine engine: ClientInGameEngine, f: LuaFunc) {
        engine.tickFunctions.add(f)
    }

    /**
     * HUD를 생성합니다.
     */
    @LuaFunction("create_hud")
    fun createHud() = HudRenderer()

    /**
     * 모든 열려있는 HUD를 닫습니다.
     */
    @LuaFunction("clear_opened_hud")
    fun clearHud(@RetrieveEngine engine: ClientInGameEngine) = engine.enabledHud.clear()

    /**
     * 서버로부터 전송받은 문자열 데이터를 가져옵니다.
     */
    @LuaFunction("remote_string_data")
    fun getStringData(@RetrieveEngine engine: ClientInGameEngine, of: String) = engine.clientStringData[of] ?: "null"

    /**
     * 서버로부터 전송받은 정수 데이터를 가져옵니다.
     */
    @LuaFunction("remote_number_data")
    fun getNumberData(@RetrieveEngine engine: ClientInGameEngine, of: String) = engine.clientNumberData[of] ?: 0.0

    /**
     * 서버로부터 전송받은 아이템 데이터를 가져옵니다.
     */
    @LuaFunction("remove_item_data")
    fun getItemData(@RetrieveEngine engine: ClientInGameEngine, of: String) = LuaItemStack(engine.clientItemStackData[of] ?: ItemStack.EMPTY)

    /**
     * 새로 추가한 조작키를 실행할때 실행될 함수를 지정합니다.
     * @param key 누를 키
     * @param function 실행할 함수
     */
    @LuaFunction("add_on_key_pressed")
    fun onKeyPressed(@RetrieveEngine engine: ClientInGameEngine, key: String, function: LuaFunc) = engine.registerKeyHook(key, function)
}