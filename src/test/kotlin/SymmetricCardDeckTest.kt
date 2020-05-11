import models.cards.SymmetricCard
import models.decks.Deck
import models.decks.SymmetricCardDeck
import org.junit.Before
import org.junit.Test

class SymmetricCardDeckTest {

    private lateinit var deck: Deck

    @Before
    internal fun beforeAll() {
        deck = SymmetricCardDeck()
    }

    @Test
    fun `init deck`() {
        assert(deck.size() == 20)
    }

    @Test
    fun `add card to beginning of the deck`() {
        val card = SymmetricCard(1)
        val deckSize = deck.size()
        deck.addCardToStart(card)
        assert(deck.size() == deckSize + 1)
        assert(deck.cards.first() == card)
    }

    @Test
    fun `add card to end of the deck`() {
        val card = SymmetricCard(1)
        val deckSize = deck.size()
        deck.addCardToEnd(card)
        assert(deck.size() == deckSize + 1)
        assert(deck.cards.last() == card)
    }
}
