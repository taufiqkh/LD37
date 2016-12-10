package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

/**
 * Entity representing the player
 */
class PlayerEntity : Entity() {
	val position = Vector2()
		get() = field.cpy()
	val velocity = Vector2()
		get() = field.cpy()

	fun move(movement: Vector2) {
		position.add(movement)
	}
}