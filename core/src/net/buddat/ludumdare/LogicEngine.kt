package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine

/**
 * Powers the entity handling
 */
class LogicEngine : Runnable {
    val engine: Engine = Engine()
	
	override fun run() {
		println("Hello World")
	}
}