package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

/**
 * Entity representing the player
 */
class PlayerEntity : Entity() {
	private val _position = Vector2()
	val position: Vector2
		get() = _position.cpy()
	private val _velocity = Vector2()
	val velocity: Vector2
		get() = _velocity.cpy()

	var isAirborne: Boolean = false
	var isMoving: Boolean = false
	var isLanding: Boolean = false
	var isIdle: Boolean = true

	fun move(aVelocity: Vector2, movement: Vector2) {
		_velocity.set(aVelocity)
		_position.add(movement)
	}
}