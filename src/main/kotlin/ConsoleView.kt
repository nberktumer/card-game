import listeners.GameStatusChangeListener
import listeners.PlayerEventListener
import listeners.PlayerHealthChangeListener
import models.GameStatus
import models.Match
import models.Player
import models.cards.Card
import kotlin.math.abs

class ConsoleView {
    private var match: Match

    /**
     * Initialize the game
     */
    init {
        println("Enter the first player's name:")
        print("> ")
        val firstPlayerName: String = getPlayerName()

        println("Enter the second player's name:")
        print("> ")
        val secondPlayerName: String = getPlayerName()

        val player1 = Player(firstPlayerName, Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance())
        val player2 = Player(secondPlayerName, Constants.DEFAULT_DECK_CLASS.getConstructor().newInstance())

        val playerHealthChangeListener = object : PlayerHealthChangeListener {
            override fun onHealthChanged(player: Player, amount: Int) {
                when {
                    amount < -1 -> println("$player received ${abs(amount)} damage points. Remaining Health: ${player.health}")
                    amount == -1 -> println("$player received ${abs(amount)} damage point. Remaining Health: ${player.health}")
                    amount == 1 -> println("$player restored $amount health point. Remaining Health: ${player.health}")
                    amount > 1 -> println("$player restored $amount health points. Remaining Health: ${player.health}")
                }
                if (player.health <= 0) {
                    println("$player has been defeated!")
                }
            }
        }

        val playerEventListener = object : PlayerEventListener {
            override fun onCardDrawn(player: Player, card: Card) {
                println("$player has drawn $card.")
            }

            override fun onOverload(player: Player, card: Card) {
                println("$player is overloaded. $card will be discarded.")
            }

            override fun onCardPlayed(player: Player, card: Card) {
                println("$player used ${card.manaCost} mana and played $card.")
                println("$player's Mana: ${player.mana} / ${player.manaSlots}")
            }

            override fun onNotEnoughMana(player: Player, card: Card) {
                println("$player does not have enough mana to play $card.")
            }

            override fun onBleedingDamage(player: Player) {
                println("$player received bleeding damage.")
            }
        }

        player1.addPlayerHealthChangeListener(playerHealthChangeListener)
        player1.addPlayerEventListener(playerEventListener)
        player2.addPlayerHealthChangeListener(playerHealthChangeListener)
        player2.addPlayerEventListener(playerEventListener)

        match = Match(player1, player2)
        match.addGameStatusChangeListener(object : GameStatusChangeListener {
            override fun onGameStatusChanged(status: GameStatus) {
                when (status) {
                    GameStatus.LOADING -> println("Match is loading...")
                    GameStatus.STARTED -> println("Match has begun!")
                    GameStatus.ENDED -> println("Match has ended!")
                }
            }

            override fun willStartNextTurn(player: Player) {
                println("\n----------------\n")
                println("$player's turn")
            }

            override fun onNextTurn(player: Player) {
                printPlayerStatus(player)
                println("---")
                printPlayerStatus(match.getOpponent())
                playTurn(player)
            }
        })
        match.start()
    }

    /**
     * Prints the current player's info and asks for an input to play a card or end the turn
     * @param player Current player
     */
    private fun playTurn(player: Player) {
        while (match.gameStatus == GameStatus.STARTED) {
            printCards()
            print("Type card index to play or type 'pass' to pass the turn > ")
            val cardIndex = getCardIndex()
            if (cardIndex < 0) break
            if (cardIndex >= player.hand.size) {
                println("Invalid card!")
                continue
            }

            player.playCard(player.hand[cardIndex], match.getOpponent())
        }
    }

    /**
     * Prints the specified player's info
     * @param player Player to show info
     */
    private fun printPlayerStatus(player: Player) {
        println("$player's info")
        println("Deck Size: ${player.deck.size()}")
        println("Current Health: ${player.health}")
        println("Current Mana Slots: ${player.manaSlots}")
        println("Current Mana: ${player.mana}")
    }

    /**
     * Parses the read input and returns player name
     * @return Player name
     */
    private fun getPlayerName(): String {
        var playerName: String? = null
        while (playerName.isNullOrBlank()) {
            playerName = readLine()
            if (playerName.isNullOrEmpty()) {
                println("Invalid input!")
                print("> ")
            }
        }
        return playerName
    }

    /**
     * Parses the read input
     * Either ends the current turn or returns the card index
     * @return -1: End the turn
     * @return >= 0: Card index
     */
    private fun getCardIndex(): Int {
        var cardIndex: Int? = null
        do {
            try {
                val line = readLine()
                if (line == "pass") return -1

                cardIndex = line?.toInt()
            } catch (e: NumberFormatException) {
                println("Invalid input!")
                print("> ")
                continue
            }
        } while (cardIndex == null)
        return cardIndex
    }


    /**
     * Prints the current player's hand
     */
    private fun printCards() {
        println("\nCards:")
        match.currentPlayer.hand.forEachIndexed { index, card ->
            println("$index - $card")
        }
    }
}
