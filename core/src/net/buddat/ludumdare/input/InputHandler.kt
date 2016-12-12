package net.buddat.ludumdare.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/**
 * Checks relevant input for provision eg. to a movement controller
 */
class InputHandler(val keyMapping: KeyMappingConfig = KeyMappingConfig()) {
	data class InputResult(val up: Boolean, val down: Boolean,
						   val left: Boolean, val right: Boolean,
						   val jump: Boolean, val restart: Boolean)
	fun poll(): InputResult {
		return InputResult(
				isAnyPressed(keyMapping.up),
				isAnyPressed(keyMapping.down),
				isAnyPressed(keyMapping.left),
				isAnyPressed(keyMapping.right),
				isAnyPressed(keyMapping.jump),
				isAnyPressed(keyMapping.restart))
	}

	private fun isAnyPressed(key: KeyMapping): Boolean {
		return key.keys.any { Gdx.input.isKeyPressed(it) }
	}
}

