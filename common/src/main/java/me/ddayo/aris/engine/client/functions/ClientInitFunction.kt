package me.ddayo.aris.engine.client.functions

import me.ddayo.aris.client.KeyBindingHelper
import me.ddayo.aris.engine.client.ClientInitEngine
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.KeyMapping

@Environment(EnvType.CLIENT)
@LuaProvider(ClientInitEngine.PROVIDER, library = "aris.init.client")
object ClientInitFunction {
    /**
     * 새로운 조작키를 추가합니다.
     * @param key key 이름
     * @param code keycode, https://www.glfw.org/docs/3.3/group__keys.html 에서 찾을 수 있습니다.
     * @param category key 카테고리
     */
    @LuaFunction("create_keybinding")
    fun createKeyBinding(key: String, code: Int, category: String) {
        KeyBindingHelper.register(KeyMapping(key, code, category))
    }
}