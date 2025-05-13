package me.ddayo.aris.fabriclike

import me.ddayo.aris.Aris
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
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
            Aris.registerCommand(dispatcher, registry)
        }
        ServerNetworking.register()
    }
}
