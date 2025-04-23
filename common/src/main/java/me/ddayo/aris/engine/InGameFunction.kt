package me.ddayo.aris.engine

import me.ddayo.aris.Aris
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.luagen.RetrieveEngine


@LuaProvider(InGameEngine.PROVIDER, library = "aris.game")
object InGameFunction {
    /**
     * 추가한 아이템을 사용했을때 실행할 함수를 추가합니다.
     * @param item 아이템 id
     * @param func 실행할 함수
     */
    @LuaFunction("add_on_use_item")
    fun onUseItemHook(@RetrieveEngine engine: InGameEngine, item: String, func: LuaFunc) {
        engine.itemUseHook.getOrPut(item) { mutableListOf() }.add(func)
    }

    /**
     * add_on_use_item을 통해 등록한 함수들을 초기화합니다.
     * @param item 초기화할 아이템
     */
    @LuaFunction("clear_on_use_item")
    fun clearOnUseItem(@RetrieveEngine engine: InGameEngine, item: String) {
        engine.itemUseHook[item] = mutableListOf()
    }

    /**
     * 서버 콘솔에서 커멘드를 실행합니다.
     * @param command 실행할 명령어
     */
    @LuaFunction("dispatch_command")
    fun dispatchCommand(command: String) {
        val server = Aris.server
        val dispatcher = server.commands.dispatcher
        val results = dispatcher.parse(command, server.createCommandSourceStack())
        server.commands.performCommand(results, command)
    }
}