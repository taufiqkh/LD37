package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * Entity representing the player
 */
class PlayerEntity(val body: Body) : Entity() {
	val position: Vector2
		get() = body.position.cpy()

	var isAirborne: Boolean = false
	var isMoving: Boolean = false
	var movementDirLeft: Boolean = false
	var isLanding: Boolean = false
	var isIdle: Boolean = true
}