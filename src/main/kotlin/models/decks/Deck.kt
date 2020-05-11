package models.decks

import models.cards.Card
import java.util.*

abstract class Deck(cards: List<Card> = LinkedList()) {
    val cards: List<Card>
        get() = _cards

    private var _cards: LinkedList<Card> = cards as LinkedList<Card>

    /**
     * Number of cards in the deck
     * @return Number of cards in the deck
     */
    fun size(): Int {
        return cards.count()
    }

    /**
     * Shuffles the cards in the deck
     */
    fun shuffle() {
        _cards.shuffle()
    }

    /**
     * Draws a new card from the deck
     * @return The first card in the deck
     */
    fun drawCard(): Card? {
        return _cards.poll()
    }

    /**
     * Adds the specified card to the end of the deck
     * @param card Card to append to the deck
     */
    fun addCardToEnd(card: Card) {
        _cards.addLast(card)
    }

    /**
     * Adds the specified card to the start of the deck
     * @param card Card to prepend to the deck
     */
    fun addCardToStart(card: Card) {
        _cards.addFirst(card)
    }
}
