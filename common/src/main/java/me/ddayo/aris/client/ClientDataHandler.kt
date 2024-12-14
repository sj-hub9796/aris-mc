package me.ddayo.aris.client

import net.minecraft.world.item.ItemStack

object ClientDataHandler {
    val clientStringData = mutableMapOf<String, String>()
    val clientNumberData = mutableMapOf<String, Double>()
    val clientItemStackData = mutableMapOf<String, ItemStack>()
}