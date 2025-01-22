package me.ddayo.aris.engine.client.functions

import me.ddayo.aris.client.KeyBindingHelper
import me.ddayo.aris.engine.client.ClientInitEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.KeyMapping

@Environment(EnvType.CLIENT)
@LuaProvider(ClientInitEngine.PROVIDER, library = "aris_client_init")
object ClientInitFunction {
    @LuaFunction("create_keybinding")
    fun createKeyBinding(key: String, code: Int, category: String) {
        KeyBindingHelper.register(KeyMapping(key, code, category))
    }
}