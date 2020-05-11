package models

import Constants
import listeners.PlayerEventListener
import listeners.PlayerHealthChangeListener
import models.cards.Card
import models.decks.Deck
import kotlin.math.max
import kotlin.math.min

class Player(val name: String, val deck: Deck) {
    private val playerHealthChangeListeners: ArrayList<PlayerHealthChangeListener> = arrayListOf()
    private val playerEventListeners: ArrayList<PlayerEventListener> = arrayListOf()
    private val _hand: ArrayList<Card> = arrayListOf()

    var health: Int = Constants.INITIAL_PLAYER_HEALTH
        private set(value) {
            val oldValue = field
            field = min(value, Constants.MAX_PLAYER_HEALTH)
            playerHealthChangeListeners.forEach { it.onHealthChanged(this, value - oldValue) }
        }

    var mana: Int = Constants.INITIAL_PLAYER_MANA
        private set(value) {
            field = max(min(value, manaSlots), 0)
        }

    var manaSlots: Int = Constants.INITIAL_PLAYER_MANA_SLOTS
        private set(value) {
            field = max(min(value, Constants.MAX_MANA_SLOTS), 0)
        }

    val hand: List<Card>
        get() = _hand

    init {
        deck.shuffle()
        drawCards(Constants.INITIAL_HAND_SIZE)
    }

    /**
     * Restores health
     * @param amount Amount of health points to restore
     */
    fun restoreHealth(amount: Int) {
        if (amount < 0) return
        health += amount
    }

    /**
     * Deals damage
     * @param amount Amount of health points to damage
     */
    fun dealDamage(amount: Int) {
        if (amount < 0) return
        health -= amount
    }

    /**
     * Adds mana slot
     * @param amount Amount of mana slots to add
     */
    fun addManaSlot(amount: Int) {
        if (amount < 0) return
        manaSlots += amount
    }

    /**
     * Removes mana slot
     * @param amount Amount of mana slots to remove
     */
    fun removeManaSlot(amount: Int) {
        if (amount < 0) return
        manaSlots -= amount
    }

    /**
     * Fills the mana points
     */
    fun fillMana() {
        mana = manaSlots
    }

    /**
     * Removes mana points
     * @param amount Amount of mana points to remove
     */
    fun removeMana(amount: Int) {
        if (amount < 0) return
        mana -= amount
    }

    /**
     * Adds mana points
     * @param amount Amount of mana points to add
     */
    fun addMana(amount: Int) {
        if (amount < 0) return
        mana += amount
    }

    /**
     * Registers the specified player health change listener
     * @param listener Player health change listener to register
     */
    fun addPlayerHealthChangeListener(listener: PlayerHealthChangeListener) {
        playerHealthChangeListeners.add(listener)
    }

    /**
     * Removes the specified player health change listener
     * @param listener Player health change listener to remove
     */
    fun removePlayerHealthChangeListener(listener: PlayerHealthChangeListener) {
        playerHealthChangeListeners.remove(listener)
    }

    /**
     * Registers the specified player event listener
     * @param listener Player event listener to register
     */
    fun addPlayerEventListener(listener: PlayerEventListener) {
        playerEventListeners.add(listener)
    }

    /**
     * Removes the specified player event listener
     * @param listener Player event listener to remove
     */
    fun removePlayerEventListener(listener: PlayerEventListener) {
        playerEventListeners.remove(listener)
    }

    /**
     * Draws cards
     * @param amount Number of cards to draw
     */
    fun drawCards(amount: Int) {
        (1..amount).forEach { _ ->
            val card = deck.drawCard()

            if (card == null) {
                // models.Player has no models.decks.cards left, deal bleeding damage
                dealDamage(Constants.BLEEDING_DAMAGE)
                playerEventListeners.forEach { it.onBleedingDamage(this) }
            } else {
                addCardToHand(card)
            }
        }
    }

    /**
     * Plays the specified card against to the specified player
     * @param card Card to play
     * @param target Player to whom the card will be applied?
     */
    fun playCard(card: Card, target: Player) {
        if (!hand.contains(card)) return

        if (mana >= card.manaCost) {
            removeMana(card.manaCost)
            removeCardFromHand(card)
            playerEventListeners.forEach { it.onCardPlayed(this, card) }
            card.play(target)
        } else {
            playerEventListeners.forEach { it.onNotEnoughMana(this, card) }
        }
    }

    /**
     * Adds the specified card to player's hand
     * @param card Card to add to player's hand
     */
    fun addCardToHand(card: Card) {
        // Check if player is overloaded
        if (_hand.size < Constants.OVERLOAD_LIMIT) {
            // Add the drawn card to player's hand
            _hand.add(card)
            playerEventListeners.forEach { it.onCardDrawn(this, card) }
        } else {
            // player is overloaded
            playerEventListeners.forEach { it.onOverload(this, card) }
        }
    }

    /**
     * Removed the specified card from the player's hand
     * @param card Card to remove from the player's hand
     */
    fun removeCardFromHand(card: Card) {
        _hand.remove(card)
    }

    override fun toString(): String {
        return name
    }
}
