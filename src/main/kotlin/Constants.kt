import models.decks.SymmetricCardDeck

object Constants {
    const val MAX_PLAYER_HEALTH: Int = 30
    const val INITIAL_PLAYER_HEALTH: Int = 30

    const val MAX_MANA_SLOTS: Int = 10
    const val INITIAL_PLAYER_MANA_SLOTS: Int = 0
    const val INITIAL_PLAYER_MANA: Int = 0
    const val MANA_SLOT_GAIN_AMOUNT: Int = 1

    const val DECK_SIZE: Int = 20
    const val INITIAL_HAND_SIZE: Int = 3

    const val OVERLOAD_LIMIT: Int = 5
    const val BLEEDING_DAMAGE: Int = 1

    val DEFAULT_DECK_CLASS: Class<SymmetricCardDeck> = SymmetricCardDeck::class.java
}
