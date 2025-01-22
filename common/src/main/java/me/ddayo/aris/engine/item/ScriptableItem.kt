package me.ddayo.aris.engine.item

import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.wrapper.LuaItemStack
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.util.ListExtensions.mutableForEach
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.apache.logging.log4j.LogManager

class ScriptableItem(private val name: String, property: Properties) : Item(property) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(interactionHand))

        InGameEngine.INSTANCE?.itemUseHook?.let {
            it[name]?.mutableForEach {
                it.call(
                    LuaServerPlayer(player as ServerPlayer),
                    LuaItemStack(player.getItemInHand(interactionHand))
                )
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}