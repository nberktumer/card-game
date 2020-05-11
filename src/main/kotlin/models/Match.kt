package models

import Constants
import listeners.GameStatusChangeListener
import listeners.PlayerHealthChangeListener
import kotlin.random.Random

class Match(player1: Player, player2: Player) {
    private val gameStatusChangeListeners: ArrayList<GameStatusChangeListener> = arrayListOf()
    val players: List<Player> = listOf(player1, player2)

    var gameStatus: GameStatus = GameStatus.LOADING
        private set(value) {
            if (field != value) {
                field = value
                gameStatusChangeListeners.forEach { it.onGameStatusChanged(value) }
            }
        }

    var currentPlayer: Player = players.first()
        get() = players[currentPlayerIndex]
        private set

    private var currentPlayerIndex: Int = 0

    init {
        currentPlayerIndex = getRandomPlayerIndex()
        players.forEach {
            it.addPlayerHealthChangeListener(object : PlayerHealthChangeListener {
                override fun onHealthChanged(player: Player, amount: Int) {
                    if (player.health <= 0) {
                        gameStatus = GameStatus.ENDED
                    }
                }
            })
        }
    }

    /**
     * Starts the game loop
     */
    fun start() {
        gameStatus = GameStatus.STARTED
        while (gameStatus == GameStatus.STARTED) {
            nextTurn()
        }
    }


    /**
     * Ends the current turn and starts the next turn
     */
    private fun nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.count()
        gameStatusChangeListeners.forEach { it.willStartNextTurn(currentPlayer) }

        currentPlayer.let {
            it.addManaSlot(Constants.MANA_SLOT_GAIN_AMOUNT)
            it.fillMana()
            it.drawCards(1)
        }
        gameStatusChangeListeners.forEach { it.onNextTurn(currentPlayer) }
    }

    /**
     * Returns the opponent player
     * @return Opponent player
     */
    fun getOpponent(): Player {
        return players[(currentPlayerIndex + 1) % players.count()]
    }

    /**
     * Registers the specified game status change listener
     * @param listener Game Status Change Listener
     */
    fun addGameStatusChangeListener(listener: GameStatusChangeListener) {
        gameStatusChangeListeners.add(listener)
    }

    /**
     * Removes the specified game status change listener
     * @param listener Game Status Change Listener
     */
    fun removeGameStatusChangeListener(listener: GameStatusChangeListener) {
        gameStatusChangeListeners.remove(listener)
    }

    /**
     * Returns a random player index
     * @return Random player index
     */
    private fun getRandomPlayerIndex(): Int {
        return Random.nextInt(players.count())
    }
}
