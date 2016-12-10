package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.Room

/**
 * Powers the entity handling
 */
class LogicEngine : Runnable {
	val engine: Engine = Engine()

	init {
		engine.addEntity(Room())
		engine.addEntity(PlayerEntity())
	}

	override fun run() {
		println("Hello World")
	}
}