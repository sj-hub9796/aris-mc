package me.ddayo.aris

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.command.CommandBuilderFunctions
import me.ddayo.aris.networking.NetworkingExtensions.sendDataPacket
import me.ddayo.aris.networking.NetworkingExtensions.sendReloadPacket
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.server.MinecraftServer
import party.iroiro.luajava.luajit.LuaJit


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

    fun init() {
        val engine = InitEngine.create(LuaJit())
        while(engine.tasks.isNotEmpty()) {
            engine.loop()
            engine.removeAllFinished()
        }
    }

    lateinit var server: MinecraftServer
        private set

    fun onServerStart(server: MinecraftServer) {
        this.server = server
        InGameEngine.createEngine(LuaJit())
    }

    fun onServerTick() {
        InGameEngine.INSTANCE?.loop()
    }

    fun reloadEngine() {
        InGameEngine.INSTANCE?.run {
            InGameEngine.disposeEngine()
            InGameEngine.createEngine(LuaJit())
        }
    }

    fun registerCommand(dispatcher: CommandDispatcher<CommandSourceStack>, registry: CommandBuildContext) {
        dispatcher.register(
            literal("aris")
                .then(literal("reload")
                    .executes {
                        reloadEngine()
                        server.playerList.players.forEach {
                            it.sendReloadPacket()
                        }
                        1
                    })
                .then(
                    literal("set_value")
                        .then(
                            argument("player", EntityArgument.players())
                                .then(
                                    argument("of", StringArgumentType.string())
                                        .then(
                                            literal("item")
                                                .then(argument("item", ItemArgument.item(registry))
                                                    .executes {
                                                        val of = StringArgumentType.getString(it, "of")
                                                        val item = ItemArgument.getItem(it, "item")
                                                        EntityArgument.getPlayers(it, "player").forEach {
                                                            it.sendDataPacket(of, item)
                                                        }
                                                        1
                                                    })
                                        )
                                        .then(
                                            literal("number")
                                                .then(argument("number", DoubleArgumentType.doubleArg())
                                                    .executes {
                                                        val of = StringArgumentType.getString(it, "of")
                                                        val num = DoubleArgumentType.getDouble(it, "number")
                                                        EntityArgument.getPlayers(it, "player").forEach {
                                                            it.sendDataPacket(of, num)
                                                        }
                                                        1
                                                    })
                                        )
                                        .then(
                                            literal("string")
                                                .then(argument("string", StringArgumentType.string())
                                                    .executes {
                                                        val of = StringArgumentType.getString(it, "of")
                                                        val str = StringArgumentType.getString(it, "string")
                                                        EntityArgument.getPlayers(it, "player").forEach {
                                                            it.sendDataPacket(of, str)
                                                        }
                                                        1
                                                    })
                                        )
                                )
                        )
                )
        )

        CommandBuilderFunctions.register(dispatcher)
    }
}