package listeners

import models.GameStatus
import models.Player

interface GameStatusChangeListener {
    /**
     * Called when the game status is changed
     * @param status Game status
     */
    fun onGameStatusChanged(status: GameStatus)

    /**
     * Called before the next turn is started
     * @param player Player who will play the next turn
     */
    fun willStartNextTurn(player: Player)

    /**
     * Called when the next turn is started
     * @param player Player who will play the next turn
     */
    fun onNextTurn(player: Player)
}
