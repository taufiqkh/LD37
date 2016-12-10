package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

/**
 * Entity representing the player
 */
class PlayerEntity : Entity() {
    val position = Position(Vector2())
    val velocity = Vector2()

    init {
        add(position)
    }
}