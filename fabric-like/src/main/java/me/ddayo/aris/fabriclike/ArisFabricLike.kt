package me.ddayo.aris.fabriclike

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.aris.Aris
import me.ddayo.aris.fabriclike.ServerNetworking.sendDataPacket
import me.ddayo.aris.fabriclike.ServerNetworking.sendOpenScriptPacket
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.item.ItemArgument

object ArisFabricLike {
    fun init() {
        Aris.init()
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            Aris.onServerStart(server)
        }
        ServerTickEvents.START_SERVER_TICK.register {
            Aris.onServerTick()
        }
        CommandRegistrationCallback.EVENT.register { dispatcher, registry, _ ->
            dispatcher.register(
                literal("aris")
                    .then(
                        literal("start")
                            .then(
                                argument("player", EntityArgument.players())
                                    .then(argument("script", StringArgumentType.string())
                                        .executes {
                                            val players = EntityArgument.getPlayers(it, "player")
                                            val script = StringArgumentType.getString(it, "script")
                                            players.forEach { p ->
                                                p.sendOpenScriptPacket(
                                                    ServerNetworking.Operation.OPEN,
                                                    ServerNetworking.EngineSpace.IN_GAME,
                                                    script
                                                )
                                            }
                                            1
                                        })
                            )
                    )
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

        }
        ServerNetworking.register()
    }
}
