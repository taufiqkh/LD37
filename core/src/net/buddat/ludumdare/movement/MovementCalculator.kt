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
	fun rawMovement(input: InputHandler.InputResult, player: PlayerEntity) {
		val (up, down, left, right, jump) = input
		// base movement
		val bodyPosn = player.body.position
		val velX = player.body.linearVelocity.x
		if (right xor left) {
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

		var yMovement = calcRawYMovement(input, player)

		if (jump) player.isAirborne = true
		else if (player.isAirborne) yMovement = applyGravity(yMovement)
	}

	fun calcRawYMovement(input: InputHandler.InputResult, player: PlayerEntity): Float {
		val (up, down, left, right, jump) = input
		return if (player.isAirborne) player.velocity.y
			else if (jump) jumpSpeed
			else if (up && !down) movementSpeed
			else if (down && !up) -movementSpeed
			else 0f
	}

	fun applyGravity(verticalMovement: Float): Float = verticalMovement - gravity
}