package me.ddayo.aris.client.gui.element

interface IClickableElement {
    fun clicked(mx: Double, my: Double, button: Int): Boolean
}