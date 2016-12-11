package net.buddat.ludumdare.movement

import net.buddat.ludumdare.entity.FixedBlock
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.Speed.horizontalImpulse
import net.buddat.ludumdare.movement.Speed.jumpSpeed

/**
 * Calculates raw movement impulses, irrespective of obstacles
 */
class MovementCalculator() {
	private val airborneUpThreshold = 0.01f
	private val airborneDownThreshold = -0.01f

	fun applyHorizonalImpulse(left: Boolean, right: Boolean, player: PlayerEntity) {
		val velX = player.body.linearVelocity.x
		if (right xor left) {
			val bodyPosn = player.body.position
			if (right && velX < Speed.maxHorzVelocity ||
					left && velX > -Speed.maxHorzVelocity) {
				player.isRunning = true
				val impulse = if (right) horizontalImpulse
				else -horizontalImpulse
				player.movementDirLeft = impulse < 0
				player.body.applyLinearImpulse(impulse, 0f, bodyPosn.x, bodyPosn.y, true)
			}
		} else if (velX == 0f) {
			player.isRunning = false;
		}
	}

	fun applyVerticalImpulse(jump: Boolean, player: PlayerEntity): MovementResult {
		val velY = player.body.linearVelocity.y
		if (jump && velY <= airborneUpThreshold && velY >= airborneDownThreshold &&
				player.feetContacts.isNotEmpty()) {
			val bodyPosn = player.body.position
			player.body.applyLinearImpulse(0f, jumpSpeed, bodyPosn.x, bodyPosn.y, true)
			player.isAirborne = true
			return MovementResult(player.feetContacts.first(), null)
		}
		if (velY <= airborneUpThreshold && velY >= airborneDownThreshold && player.feetContacts.isNotEmpty()) {
			if (player.isAirborne) {
				player.isAirborne = false
				return MovementResult(null, player.feetContacts.first())
			}
		}
		return MovementResult(null, null)
	}

	fun rawMovement(input: InputHandler.InputResult, player: PlayerEntity): MovementResult {
		val (up, down, left, right, jump) = input
		// base movement
		applyHorizonalImpulse(left, right, player)
		return applyVerticalImpulse(jump, player)
	}

	data class MovementResult(val jump: FixedBlock?, val land: FixedBlock?) {

	}
}