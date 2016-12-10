package net.buddat.ludumdare.input

import com.badlogic.gdx.Input

/**
 * Configuration for keyboard mapping
 */
data class Keymapping(val up: Int = Keymapping.DEFAULT_UP,
					  val down: Int = Keymapping.DEFAULT_DOWN,
					  val left: Int = Keymapping.DEFAULT_LEFT,
					  val right: Int = Keymapping.DEFAULT_RIGHT,
					  val jump: Int = Keymapping.DEFAULT_JUMP) {
	companion object {
		const val DEFAULT_UP = Input.Keys.A
		const val DEFAULT_DOWN = Input.Keys.S
		const val DEFAULT_LEFT = Input.Keys.A
		const val DEFAULT_RIGHT = Input.Keys.D
		const val DEFAULT_JUMP = Input.Keys.SPACE
	}

}