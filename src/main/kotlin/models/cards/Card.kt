package models.cards

import models.Player

abstract class Card(val name: String, val description: String, val manaCost: Int) {
    /**
     * Applies the effect of the card to the specified target
     * @param target Target player to whom the card will be applied
     */
    abstract fun play(target: Player)

    override fun toString(): String {
        return "$name ($manaCost) - $description"
    }
}
