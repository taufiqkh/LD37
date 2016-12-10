package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.Room
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.input.KeyMappingConfig

/**
 * Powers the entity handling
 */
class LogicEngine {
	val engine: Engine = Engine()
	val player: PlayerEntity = PlayerEntity()
	val inputHandler = InputHandler()

	init {
		engine.addEntity(Room())
		engine.addEntity(player)
	}

	fun getPlayerPosn(): Vector2 {
		return player.position
	}

	val movementSpeed = 10f

	fun update(delta: Float) {
		val (up, down, left, right, jump) = inputHandler.poll()
		val xMovement = if (right && !left) movementSpeed
			else if (left && !right) -movementSpeed
			else 0f
		val yMovement = if (up && !down) movementSpeed
			else if (down && !up) -movementSpeed
			else 0f
		val movement = Vector2(xMovement, yMovement).scl(delta)

		player.move(movement)
	}
}