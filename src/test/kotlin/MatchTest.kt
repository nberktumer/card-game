import listeners.GameStatusChangeListener
import models.GameStatus
import models.Match
import models.Player
import org.junit.Before
import org.junit.Test

class MatchTest {

    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var match: Match

    @Before
    internal fun beforeAll() {
        val defaultDeck1 = Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance()
        val defaultDeck2 = Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance()
        val playerName1 = "player1"
        val playerName2 = "player2"

        player1 = Player(playerName1, defaultDeck1)
        player2 = Player(playerName2, defaultDeck2)

        match = Match(player1, player2)
    }

    @Test
    fun `init match`() {
        match.let {
            assert(it.players.size == 2)
            assert(it.players.contains(player1))
            assert(it.players.contains(player2))
            assert(it.gameStatus == GameStatus.LOADING)
        }
    }

    @Test
    fun `get opponent player`() {
        val currentPlayer = match.currentPlayer
        val opponent = match.getOpponent()
        assert(opponent != currentPlayer)
    }

    @Test
    fun `start game`() {
        assert(match.gameStatus == GameStatus.LOADING)
        var isFirst = true
        var lastPlayer = match.currentPlayer
        match.addGameStatusChangeListener(object : GameStatusChangeListener {
            override fun onGameStatusChanged(status: GameStatus) {
                if (isFirst) {
                    assert(match.gameStatus == GameStatus.STARTED)
                    isFirst = false
                }
            }

            override fun willStartNextTurn(player: Player) {
                assert(player != lastPlayer)
                lastPlayer = player
            }

            override fun onNextTurn(player: Player) {
                assert(player == lastPlayer)
            }

        })
        match.start()
    }

    @Test
    fun `remove listener`() {
        var isCalled = false
        val listener = object : GameStatusChangeListener {
            override fun onGameStatusChanged(status: GameStatus) {
                isCalled = true
            }

            override fun willStartNextTurn(player: Player) {
                isCalled = true
            }

            override fun onNextTurn(player: Player) {
                isCalled = true
            }

        }

        match.addGameStatusChangeListener(listener)
        match.removeGameStatusChangeListener(listener)
        match.start()
        assert(!isCalled)
    }
}
