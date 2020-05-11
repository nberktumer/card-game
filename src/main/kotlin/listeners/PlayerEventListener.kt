package listeners

import models.Player
import models.cards.Card

interface PlayerEventListener {
    /**
     * Called when a new card is drawn
     * @param player Player who drew the card
     * @param card The drawn card
     */
    fun onCardDrawn(player: Player, card: Card)

    /**
     * Called when the player has too much cards in his/her hand.
     * The drawn card will be discarded.
     * @param player Player who is overloaded
     * @param card The discarded player
     */
    fun onOverload(player: Player, card: Card)

    /**
     * Called when the player plays a card
     * @param player Player who played the card
     * @param card The played card
     */
    fun onCardPlayed(player: Player, card: Card)

    /**
     * Called when the player does not have enough mana to player the specified card
     * @param player Player who tries to play the card
     * @param card The unplayable card
     */
    fun onNotEnoughMana(player: Player, card: Card)

    /**
     * Called when the player does not have any cards in his/her deck.
     * The player will receive bleeding damage.
     * @param player Player who receives bleeding damage
     */
    fun onBleedingDamage(player: Player)
}
