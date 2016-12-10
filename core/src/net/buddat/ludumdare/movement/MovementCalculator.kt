package net.buddat.ludumdare.movement

import com.badlogic.gdx.math.Vector2
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.Speed.gravity
import net.buddat.ludumdare.movement.Speed.horizontalImpulse
import net.buddat.ludumdare.movement.Speed.jumpSpeed
import net.buddat.ludumdare.movement.Speed.movementSpeed

/**
 * Calculates movement based on a delta
 */
class MovementCalculator(val delta: Float) {
	fun applyHorizonalImpulse(left: Boolean, right: Boolean, player: PlayerEntity) {
		val velX = player.body.linearVelocity.x
		if (right xor left) {
			val bodyPosn = player.body.position
			if (right && velX < Speed.maxHorzVelocity ||
					left && velX > -Speed.maxHorzVelocity) {
				player.isMoving = true
				val impulse = if (right) horizontalImpulse
				else -horizontalImpulse
				player.body.applyLinearImpulse(impulse, 0f, bodyPosn.x, bodyPosn.y, true)
			}
		} else if (velX == 0f) {
			player.isMoving = false;
		}
	}

	fun applyVerticalImpulse(jump: Boolean, player: PlayerEntity) {
		val velY = player.body.linearVelocity.y
		if (jump && velY == 0f) {
			val bodyPosn = player.body.position
			player.body.applyLinearImpulse(0f, jumpSpeed, bodyPosn.x, bodyPosn.y, true)
		}
	}

	fun rawMovement(input: InputHandler.InputResult, player: PlayerEntity) {
		val (up, down, left, right, jump) = input
		// base movement
		applyHorizonalImpulse(left, right, player)
		applyVerticalImpulse(jump, player)
	}
}