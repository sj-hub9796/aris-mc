package me.ddayo.aris

import me.ddayo.aris.engine.InitEngine
import party.iroiro.luajava.luajit.LuaJit
import java.io.File


/*
    Types of engine
    1. Client/Server init engine: modifies minecraft core
        Scripts under /robots/init will be loaded automatically into this engine.
        Scripts under /robots/client-init will be loaded automatically if client side.
        This engine is suitable for new items, new blocks for both part and
        custom keybinding for client part.
        This engine will not accessible after init progress

    2. Client main engine: does simple GUI stuff
        This only exists on Client side.
        Scripts under /robots/client will be loaded automatically into this engine.
        This engine used for after game initialized but does not require world instance.
        This engine is suitable for custom settings screen or custom main screen or etc.

    3. Client/Server in-game-engine: does in game stuff
        Scripts under /robots and not reserved with other engine will be
        loaded automatically into this engine(Except /robots/functions).
        It's free to access world instance with this engine.
        Be careful of logical side using this engine.
        This engine is suitable for rest of callback events for both part and
        custom HUD for client part.
        Only this engine can interact between server and client.


    Command concept
    /aris reload => reload entire library scripts for in-game-engine
    /aris invoke <function> (player) => run single function
    /aris eval <code> => evaluate single line using `lua.eval(code)`
    /aris set <players> <name> <type> <variable> => sync data into client, will be moved into lua later.
    /aris remote invoke <players> <function> => remote execute functions to given players
 */
object Aris {
    const val MOD_ID = "aris"
    const val VERSION = "0.1.0"

    @OptIn(ReferenceMayKeepAlive::class)
    fun init() {
        val engine = InitEngine(LuaJit())
        File("robots/init").listFiles()?.forEach {
            engine.createTask(it, it.nameWithoutExtension)
        }
        while(engine.tasks.isNotEmpty()) {
            engine.loop()
            engine.removeAllFinished()
        }
    }
    fun onServerStart() {

    }

}