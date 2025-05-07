package me.ddayo.aris.forge

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.aris.Aris
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.command.CommandBuilderFunctions
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.forge.ArisForgeNetworking.sendDataPacket
import me.ddayo.aris.forge.ArisForgeNetworking.sendOpenScriptPacket
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResultHolder
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber(modid = "aris")
object ArisForgeServerEventSubscriber {
    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
        // Initialize ARIS server
        Aris.onServerStart(event.server)
    }

    @SubscribeEvent
    fun onItemUse(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity as? ServerPlayer ?: return
        val stack = player.getItemInHand(event.hand)
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

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        val dispatcher = event.dispatcher as CommandDispatcher<CommandSourceStack>

        // Shared commands
        CommandBuilderFunctions.register(dispatcher)

        // /aris start <player> <script>
        dispatcher.register(
            literal("aris").then(
                literal("start").then(
                    argument("player", EntityArgument.players()).then(
                        argument("script", StringArgumentType.string())
                            .executes {
                                val players = EntityArgument.getPlayers(it, "player")
                                val script = StringArgumentType.getString(it, "script")
                                players.forEach { p ->
                                    p.sendOpenScriptPacket(
                                        ArisForgeNetworking.Operation.OPEN,
                                        ArisForgeNetworking.EngineSpace.IN_GAME,
                                        script
                                    )
                                }
                                1
                            }
                    )
                )
            )
        )

        // /aris set_value <player> <of> item <item>
        // /aris set_value <player> <of> number <number>
        // /aris set_value <player> <of> string <string>
        dispatcher.register(
            literal("aris").then(
                literal("set_value").then(
                    argument("player", EntityArgument.players()).then(
                        argument("of", StringArgumentType.string()).then(
                            literal("item").then(
                                argument("item", ItemArgument.item(event.buildContext))
                                    .executes {
                                        val of = StringArgumentType.getString(it, "of")
                                        val itemStack = ItemArgument.getItem(it, "item").createItemStack(1, false)
                                        EntityArgument.getPlayers(it, "player").forEach { p ->
                                            p.sendDataPacket(of, itemStack)
                                        }
                                        1
                                    }
                            )
                        ).then(
                            literal("number").then(
                                argument("number", DoubleArgumentType.doubleArg())
                                    .executes {
                                        val of = StringArgumentType.getString(it, "of")
                                        val num = DoubleArgumentType.getDouble(it, "number")
                                        EntityArgument.getPlayers(it, "player").forEach { p ->
                                            p.sendDataPacket(of, num)
                                        }
                                        1
                                    }
                            )
                        ).then(
                            literal("string").then(
                                argument("string", StringArgumentType.string())
                                    .executes {
                                        val of = StringArgumentType.getString(it, "of")
                                        val str = StringArgumentType.getString(it, "string")
                                        EntityArgument.getPlayers(it, "player").forEach { p ->
                                            p.sendDataPacket(of, str)
                                        }
                                        1
                                    }
                            )
                        )
                    )
                )
            )
        )
    }

    @SubscribeEvent
    fun onServerTick(event: TickEvent.LevelTickEvent) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) return
        Aris.onServerTick()
    }
}
