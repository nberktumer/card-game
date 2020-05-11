import models.Player
import models.cards.SymmetricCard
import org.junit.Before
import org.junit.Test

class SymmetricCardTest {

    private lateinit var card: SymmetricCard
    private lateinit var player: Player

    @Before
    internal fun beforeAll() {
        val defaultDeck = Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance()
        val playerName = "test"

        player = Player(playerName, defaultDeck)
        card = SymmetricCard(1)
    }

    @Test
    fun `init card`() {
        card.let {
            assert(it.name == "Symmetric Card")
            assert(it.description == "Deals damage equals to its mana cost")
            assert(it.manaCost == 1)
            assert(it.toString() == "${it.name} (${it.manaCost}) - ${it.description}")
        }
    }

    @Test
    fun `play card`() {
        val health = player.health
        card.play(player)
        assert(player.health == health - card.manaCost)
    }
}
