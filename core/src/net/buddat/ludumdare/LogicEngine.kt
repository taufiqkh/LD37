package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.Room
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.MovementCalculator
import net.buddat.ludumdare.movement.Speed

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

	val floor = 0f
	val ceiling = 1000f
	val leftBounds = 0f
	val rightBounds = 1000f

	// Avoid slow spiral of death on slow devices
	val minFrameTime = 0.25f

	fun update(delta: Float) {
		val frameTime = Math.min(delta, minFrameTime)
		val mCalc = MovementCalculator(delta)
		val movement = mCalc.rawMovement(inputHandler.poll(), player)

		player.move(movement, movement.cpy().scl(frameTime))
	}
}