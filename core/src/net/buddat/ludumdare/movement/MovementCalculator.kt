package net.buddat.ludumdare.movement

import com.badlogic.gdx.math.Vector2
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.Speed.gravity
import net.buddat.ludumdare.movement.Speed.jumpSpeed
import net.buddat.ludumdare.movement.Speed.movementSpeed

/**
 * Calculates movement based on a delta
 */
class MovementCalculator(val delta: Float) {
	fun rawMovement(input: InputHandler.InputResult, player: PlayerEntity): Vector2 {
		val (up, down, left, right, jump) = input
		// base movement
		val xMovement = if (right && !left) movementSpeed
		else if (left && !right) -movementSpeed
		else 0f
		var yMovement = calcRawYMovement(input, player)

		if (jump) player.isAirborne = true
		else if (player.isAirborne) yMovement = applyGravity(yMovement)
		else if (xMovement == 0f) player.isMoving = false
		else player.isMoving = true

		return Vector2(xMovement, yMovement)
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