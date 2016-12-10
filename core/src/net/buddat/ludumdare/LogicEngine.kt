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
class LogicEngine : Runnable {
	val engine: Engine = Engine()
	val player: PlayerEntity = PlayerEntity()
	val inputHandler = InputHandler(KeyMappingConfig())

	init {
		engine.addEntity(Room())
		engine.addEntity(player)
	}

	override fun run() {
		println("Hello World")
	}

	fun getPlayerPosn(): Vector2 {
		return player.position
	}
}