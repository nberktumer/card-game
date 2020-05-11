import listeners.PlayerEventListener
import listeners.PlayerHealthChangeListener
import models.Player
import models.cards.Card
import models.cards.SymmetricCard
import org.junit.Before
import org.junit.Test

class PlayerTest {

    private lateinit var player: Player

    @Before
    internal fun beforeAll() {
        val defaultDeck = Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance()
        val playerName = "test"

        player = Player(playerName, defaultDeck)
    }

    @Test
    fun `init player`() {
        player.let {
            assert(it.name == "test")
            assert(it.hand.size == Constants.INITIAL_HAND_SIZE)
            assert(it.health == Constants.INITIAL_PLAYER_HEALTH)
            assert(it.mana == Constants.INITIAL_PLAYER_MANA)
            assert(it.manaSlots == Constants.INITIAL_PLAYER_MANA_SLOTS)
        }
    }

    @Test
    fun `to string`() {
        assert(player.toString() == "test")
    }

    @Test
    fun `deal 1 damage`() {
        val playerHealth = player.health

        player.dealDamage(1)
        assert(player.health == playerHealth - 1)
    }

    @Test
    fun `deal damage more than player's health`() {
        val playerHealth = player.health

        player.dealDamage(playerHealth + 10)
        assert(player.health == -10)
    }

    @Test
    fun `deal negative damage`() {
        val playerHealth = player.health

        player.dealDamage(-5)
        assert(player.health == playerHealth)
    }

    @Test
    fun `deal zero damage`() {
        val playerHealth = player.health

        player.dealDamage(0)
        assert(player.health == playerHealth)
    }

    @Test
    fun `deal 1 damage and restore 1 health`() {
        val playerHealth = player.health
        player.dealDamage(1)
        assert(player.health == playerHealth - 1)

        player.restoreHealth(1)
        assert(player.health == playerHealth)
    }

    @Test
    fun `restore more than max health`() {
        player.restoreHealth(Constants.MAX_PLAYER_HEALTH + 10)
        assert(player.health == Constants.MAX_PLAYER_HEALTH)
    }

    @Test
    fun `restore negative health`() {
        val playerHealth = player.health

        player.restoreHealth(-5)
        assert(player.health == playerHealth)
    }

    @Test
    fun `restore zero health`() {
        val playerHealth = player.health

        player.restoreHealth(0)
        assert(player.health == playerHealth)
    }

    @Test
    fun `add 1 mana slot`() {
        val manaSlots = player.manaSlots

        player.addManaSlot(1)
        assert(player.manaSlots == manaSlots + 1)
    }

    @Test
    fun `add more than max mana slots`() {
        player.addManaSlot(Constants.MAX_MANA_SLOTS + 10)
        assert(player.manaSlots == Constants.MAX_MANA_SLOTS)
    }

    @Test
    fun `add negative mana slot`() {
        val manaSlots = player.manaSlots

        player.addManaSlot(-1)
        assert(player.manaSlots == manaSlots)
    }

    @Test
    fun `add 1 mana slot and remove 1 mana slot`() {
        val manaSlots = player.manaSlots

        player.addManaSlot(1)
        assert(player.manaSlots == manaSlots + 1)

        player.removeManaSlot(1)
        assert(player.manaSlots == manaSlots)
    }

    @Test
    fun `remove more than min mana slots`() {
        val manaSlots = player.manaSlots

        player.removeManaSlot(manaSlots + 10)
        assert(player.manaSlots == 0)
    }

    @Test
    fun `remove negative mana slots`() {
        val manaSlots = player.manaSlots

        player.removeManaSlot(-1)
        assert(player.manaSlots == manaSlots)
    }

    @Test
    fun `add max mana slots and 2 mana points`() {
        val mana = player.mana
        player.addManaSlot(Constants.MAX_MANA_SLOTS)
        assert(player.manaSlots == Constants.MAX_MANA_SLOTS)

        player.addMana(mana + 2)
        assert(player.mana == mana + 2)
    }

    @Test
    fun `add mana more than mana slots`() {
        player.addMana(player.manaSlots + 10)
        assert(player.mana == player.manaSlots)
    }

    @Test
    fun `add negative mana`() {
        val mana = player.mana
        player.addMana(-1)
        assert(player.mana == mana)
    }

    @Test
    fun `Add 1 mana slot, 1 mana and remove 1 mana`() {
        val mana = player.mana
        val manaSlot = player.manaSlots
        player.addManaSlot(manaSlot + 1)
        assert(player.manaSlots == manaSlot + 1)

        player.addMana(mana + 1)
        assert(player.mana == mana + 1)

        player.removeMana(1)
        assert(player.mana == mana)
    }

    @Test
    fun `remove more mana than available`() {
        val mana = player.mana

        player.removeMana(mana - 10)
        assert(player.mana == 0)
    }

    @Test
    fun `Add 1 mana slot, 1 mana and remove negative mana`() {
        val mana = player.mana
        val manaSlot = player.manaSlots
        player.addManaSlot(manaSlot + 1)
        assert(player.manaSlots == manaSlot + 1)

        player.addMana(mana + 1)
        assert(player.mana == mana + 1)

        player.removeMana(-10)
        assert(player.mana == mana + 1)
    }

    @Test
    fun `max mana slots and fill mana`() {
        player.addManaSlot(Constants.MAX_MANA_SLOTS)
        assert(player.manaSlots == Constants.MAX_MANA_SLOTS)

        player.fillMana()
        assert(player.mana == Constants.MAX_MANA_SLOTS)
    }

    @Test
    fun `draw 1 card`() {
        val handSize = player.hand.size
        val deckSize = player.deck.cards.size

        player.drawCards(1)

        assert(player.hand.size == handSize + 1)
        assert(player.deck.cards.size == deckSize - 1)
    }

    @Test
    fun `draw overload limit - 1 cards`() {
        val handSize = player.hand.size
        val deckSize = player.deck.cards.size

        val numCardsToDraw = Constants.OVERLOAD_LIMIT - handSize - 1

        player.drawCards(numCardsToDraw)

        assert(player.hand.size == Constants.OVERLOAD_LIMIT - 1)
        assert(player.deck.cards.size == deckSize - numCardsToDraw)
    }

    @Test
    fun overload() {
        val deckSize = player.deck.cards.size

        player.drawCards(Constants.OVERLOAD_LIMIT + 1)

        assert(player.hand.size == Constants.OVERLOAD_LIMIT)
        assert(player.deck.cards.size == deckSize - Constants.OVERLOAD_LIMIT - 1)
    }

    @Test
    fun `draw more cards than deck, overload and receive bleeding damage`() {
        val deckSize = player.deck.cards.size
        val health = player.health

        player.drawCards(deckSize + 10)

        assert(player.hand.size == Constants.OVERLOAD_LIMIT)
        assert(player.deck.cards.isEmpty())
        assert(player.health == health - 10)
    }

    @Test
    fun `draw negative cards`() {
        val handSize = player.hand.size
        val deckSize = player.deck.cards.size

        player.drawCards(-1)

        assert(player.hand.size == handSize)
        assert(player.deck.cards.size == deckSize)
    }

    @Test
    fun `draw 1 card and remove it`() {
        val hand = player.hand.toList() // copy list
        val deckSize = player.deck.cards.size

        player.drawCards(1)

        assert(player.hand.size == hand.size + 1)
        assert(player.deck.cards.size == deckSize - 1)

        val arrayDiff = (player.hand - hand)
        assert(arrayDiff.size == 1)

        val drawnCard = arrayDiff.first()
        player.removeCardFromHand(drawnCard)

        assert(player.hand == hand)
    }

    @Test
    fun `remove unavailable card`() {
        val hand = player.hand.toList() // copy list

        val card = SymmetricCard(1)
        player.removeCardFromHand(card)

        assert(player.hand == hand)
    }

    @Test
    fun `add card to player's hand`() {
        val hand = player.hand.toList() // copy list

        val card = SymmetricCard(1)
        player.addCardToHand(card)

        val diff = player.hand - hand
        assert(diff.size == 1)
        assert(diff.first() == card)
    }

    @Test
    fun `play 1 mana symmetric card`() {
        val health = player.health

        player.addManaSlot(Constants.MAX_MANA_SLOTS)
        assert(player.manaSlots == Constants.MAX_MANA_SLOTS)
        player.fillMana()
        assert(player.mana == Constants.MAX_MANA_SLOTS)

        val card = SymmetricCard(1)
        player.addCardToHand(card)

        player.playCard(card, player)

        assert(player.health == health - 1)
        assert(player.mana == Constants.MAX_MANA_SLOTS - 1)
    }

    @Test
    fun `play 1 mana symmetric card that is not in the player's hand`() {
        val health = player.health

        player.addManaSlot(Constants.MAX_MANA_SLOTS)
        assert(player.manaSlots == Constants.MAX_MANA_SLOTS)
        player.fillMana()
        assert(player.mana == Constants.MAX_MANA_SLOTS)

        val card = SymmetricCard(1)
        player.playCard(card, player)

        assert(player.health == health)
        assert(player.mana == Constants.MAX_MANA_SLOTS)
    }

    @Test
    fun `play 1 mana symmetric card with no mana`() {
        val health = player.health

        player.removeMana(player.mana)
        assert(player.mana == 0)

        val card = SymmetricCard(1)
        player.playCard(card, player)

        assert(player.health == health)
        assert(player.mana == 0)
    }

    @Test
    fun `add health change listener`() {
        val health = player.health
        val listener = object : PlayerHealthChangeListener {
            override fun onHealthChanged(player: Player, amount: Int) {
                assert(player.health == amount + health)
            }

        }
        player.addPlayerHealthChangeListener(listener)
        player.dealDamage(1)
    }

    @Test
    fun `remove health change listener`() {
        var isCalled = false
        val listener = object : PlayerHealthChangeListener {
            override fun onHealthChanged(player: Player, amount: Int) {
                isCalled = true
            }

        }
        player.addPlayerHealthChangeListener(listener)
        player.removePlayerHealthChangeListener(listener)
        player.dealDamage(1)
        assert(!isCalled)
    }

    @Test
    fun `add event listener`() {
        val initialHandSize = player.hand.size
        val health = player.health
        val listener = object : PlayerEventListener {
            override fun onCardDrawn(player: Player, card: Card) {
                assert(initialHandSize < player.hand.size)
            }

            override fun onOverload(player: Player, card: Card) {
                assert(player.hand.size == Constants.OVERLOAD_LIMIT)
                assert(!player.hand.contains(card))
            }

            override fun onCardPlayed(player: Player, card: Card) {
                assert(!player.hand.contains(card))
            }

            override fun onNotEnoughMana(player: Player, card: Card) {
                assert(player.mana < card.manaCost)
            }

            override fun onBleedingDamage(player: Player) {
                assert(player.health < health)
            }

        }
        player.addPlayerEventListener(listener)
        player.drawCards(Constants.DECK_SIZE + 5)
        player.hand.forEach {
            player.playCard(it, player)
        }
    }

    @Test
    fun `remove event listener`() {
        var isCalled = false
        val listener = object : PlayerEventListener {
            override fun onCardDrawn(player: Player, card: Card) {
                isCalled = true
            }

            override fun onOverload(player: Player, card: Card) {
                isCalled = true
            }

            override fun onCardPlayed(player: Player, card: Card) {
                isCalled = true
            }

            override fun onNotEnoughMana(player: Player, card: Card) {
                isCalled = true
            }

            override fun onBleedingDamage(player: Player) {
                isCalled = true
            }

        }
        player.addPlayerEventListener(listener)
        player.removePlayerEventListener(listener)
        player.drawCards(Constants.DECK_SIZE + 5)
        player.hand.forEach {
            player.playCard(it, player)
        }
        assert(!isCalled)
    }
}
