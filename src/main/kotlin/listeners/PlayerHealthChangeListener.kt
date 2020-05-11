package listeners

import models.Player

interface PlayerHealthChangeListener {
    /**
     * Called when the player's health is changed
     * @param player Player whose health is changed
     * @param amount Amount of changed health points
     */
    fun onHealthChanged(player: Player, amount: Int)
}
