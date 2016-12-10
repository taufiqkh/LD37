package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.Room
import net.buddat.ludumdare.input.InputHandler

/**
 * Powers the entity handling
 */
class LogicEngine {
	val engine: Engine = Engine()
	val player: PlayerEntity = PlayerEntity()
	val inputHandler = InputHandler()
	
	var currentRoom: Room

	init {
		currentRoom = Room(defaultMap)
		
		engine.addEntity(currentRoom)
		engine.addEntity(player)
	}

	fun getPlayerPosn(): Vector2 {
		return player.position
	}

	val movementSpeed = 10f

	val floor = 0f
	val ceiling = 1000f
	val leftBounds = 0f
	val rightBounds = 1000f

	fun update(delta: Float) {
		val (up, down, left, right, jump) = inputHandler.poll()

		// base movement
		val rawXMovement = if (right && !left) movementSpeed
			else if (left && !right) -movementSpeed
			else 0f
		val rawYMovement = if (up && !down) movementSpeed
			else if (down && !up) -movementSpeed
			else 0f

		// check bounds
		val yMovement = MathUtils.clamp(rawYMovement, floor, ceiling)
		val xMovement = MathUtils.clamp(rawXMovement, leftBounds, rightBounds)

		// as speed is per second, scale the speed according to the delta
		val movement = Vector2(xMovement, yMovement).scl(delta)

		player.move(movement)
	}
}