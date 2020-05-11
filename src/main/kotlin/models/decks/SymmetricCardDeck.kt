package models.decks

import models.cards.SymmetricCard
import java.util.*

class SymmetricCardDeck : Deck(
    cards = LinkedList(
        listOf(
            SymmetricCard(0),
            SymmetricCard(0),
            SymmetricCard(1),
            SymmetricCard(1),
            SymmetricCard(2),
            SymmetricCard(2),
            SymmetricCard(2),
            SymmetricCard(3),
            SymmetricCard(3),
            SymmetricCard(3),
            SymmetricCard(3),
            SymmetricCard(4),
            SymmetricCard(4),
            SymmetricCard(4),
            SymmetricCard(5),
            SymmetricCard(5),
            SymmetricCard(6),
            SymmetricCard(6),
            SymmetricCard(7),
            SymmetricCard(8)
        )
    )
)
