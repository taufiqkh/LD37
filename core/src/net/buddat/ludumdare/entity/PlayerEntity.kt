package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * Entity representing the player
 */
class PlayerEntity(val body: Body) : Entity() {
	private val _position = Vector2(body.position)
	val position: Vector2
		get() = _position.cpy()
	private val _velocity = Vector2()
	val velocity: Vector2
		get() = _velocity.cpy()

	var isAirborne: Boolean = false
	var isMoving: Boolean = false
	var isLanding: Boolean = false
	var isIdle: Boolean = true

	fun move() {
		_position.set(body.position)
	}
}