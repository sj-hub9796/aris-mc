package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaEngine
import me.ddayo.aris.engine.client.ClientInGameEngine
import me.ddayo.aris.engine.client.ClientMainEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import me.ddayo.aris.luagen.RetrieveEngine
import org.apache.logging.log4j.LogManager

/*
TODO: This may replaced into IN_GAME_ONLY
 */
@LuaProvider(ClientMainEngine.PROVIDER)
class HudRenderer: BaseRectComponent(), ILuaStaticDecl by LuaClientOnlyGenerated.HudRenderer_LuaGenerated {
    @LuaFunction(name = "open_hud")
    fun openHud(@RetrieveEngine engine: ClientInGameEngine) {
        engine.enabledHud.add(this)
    }

    @LuaFunction(name = "close_hud")
    fun closeHud(@RetrieveEngine engine: ClientInGameEngine) {
        if(!engine.enabledHud.remove(this))
            LogManager.getLogger().warn("Target hud not exists.")
    }
}