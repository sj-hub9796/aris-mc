package me.ddayo.aris.client.gui

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.client.ClientDataHandler
import me.ddayo.aris.client.engine.ClientMainEngine
import me.ddayo.aris.lua.glue.LuaClientOnlyGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import org.apache.logging.log4j.LogManager

/*
TODO: This may replaced into IN_GAME_ONLY
 */
@LuaProvider(ClientMainEngine.PROVIDER)
class HudRenderer: BaseRenderer(), ILuaStaticDecl by LuaClientOnlyGenerated.HudRenderer_LuaGenerated {
    @LuaFunction(name = "open_hud")
    fun openHud() {
        ClientDataHandler.enabledHud.add(this)
    }

    @LuaFunction(name = "close_hud")
    fun closeHud() {
        if(!ClientDataHandler.enabledHud.remove(this))
            LogManager.getLogger().warn("Target hud not exists.")
    }
}