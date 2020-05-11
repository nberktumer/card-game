package models.cards

import models.Player

class SymmetricCard(manaCost: Int) : Card("Symmetric Card", "Deals damage equals to its mana cost", manaCost) {
    override fun play(target: Player) {
        target.dealDamage(manaCost)
    }
}
