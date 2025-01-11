package me.ddayo.aris.client

import me.ddayo.aris.client.gui.HudRenderer
import net.minecraft.world.item.ItemStack

object ClientDataHandler {
    val clientStringData = mutableMapOf<String, String>()
    val clientNumberData = mutableMapOf<String, Double>()
    val clientItemStackData = mutableMapOf<String, ItemStack>()
    val enabledHud = mutableListOf<HudRenderer>()
}