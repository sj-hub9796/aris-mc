package me.ddayo.aris.fabriclike

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.aris.Aris
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.command.CommandBuilderFunctions
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.fabriclike.ServerNetworking.sendDataPacket
import me.ddayo.aris.fabriclike.ServerNetworking.sendOpenScriptPacket
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResultHolder

object ArisFabricLike {
    fun init() {
        Aris.init()
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            Aris.onServerStart(server)
        }
        ServerTickEvents.START_SERVER_TICK.register {
            Aris.onServerTick()
        }

        UseItemCallback.EVENT.register { player, world, hand ->
            val stack = player.getItemInHand(hand)
            if(!world.isClientSide) {
                InGameEngine.INSTANCE?.itemUseHook?.let {
                    it[BuiltInRegistries.ITEM.getKey(stack.item).toString()]?.let {
                        val sp = LuaServerPlayer(player as ServerPlayer)
                        val lis = LuaItemStack(stack)
                        it.mutableForEach {
                            it.callAsTask(sp, lis)
                        }
                    }
                }
            }
            InteractionResultHolder.pass(stack)
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, registry, _ ->
            CommandBuilderFunctions.register(dispatcher)

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
