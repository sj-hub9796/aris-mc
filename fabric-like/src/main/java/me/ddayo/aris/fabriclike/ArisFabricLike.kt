package me.ddayo.aris.fabriclike

import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.aris.Aris
import me.ddayo.aris.fabriclike.S2CNetworking.sendOpenScriptPacket
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument

object ArisFabricLike {
    fun init() {
        Aris.init()
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            Aris.onServerStart()
        }
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(literal("aris")
                .then(literal("start")
                    .then(argument("player", EntityArgument.players())
                        .then(argument("script", StringArgumentType.string())
                            .executes {
                                val players = EntityArgument.getPlayers(it, "player")
                                val script = StringArgumentType.getString(it, "script")
                                players.forEach { p ->
                                    p.sendOpenScriptPacket(S2CNetworking.Operation.OPEN, S2CNetworking.EngineSpace.IN_GAME, script)
                                }
                                1
                            }))))

        }
        S2CNetworking.register()
    }
}
