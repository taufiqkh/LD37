package net.buddat.ludumdare.input

import com.badlogic.gdx.Input
import java.util.*

/**
 * Configuration for keyboard mapping
 */
data class KeyMappingConfig(val up: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_UP, KeyMappingConfig.DEFAULT_UP2),
							val down: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_DOWN, KeyMappingConfig.DEFAULT_DOWN2),
							val left: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_LEFT, KeyMappingConfig.DEFAULT_LEFT2),
							val right: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_RIGHT, KeyMappingConfig.DEFAULT_RIGHT2),
							val jump: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_JUMP)) {
	companion object {
		const val DEFAULT_UP = Input.Keys.A
		const val DEFAULT_UP2 = Input.Keys.UP
		const val DEFAULT_DOWN = Input.Keys.S
		const val DEFAULT_DOWN2 = Input.Keys.DOWN
		const val DEFAULT_LEFT = Input.Keys.A
		const val DEFAULT_LEFT2 = Input.Keys.A
		const val DEFAULT_RIGHT = Input.Keys.D
		const val DEFAULT_RIGHT2 = Input.Keys.D
		const val DEFAULT_JUMP = Input.Keys.SPACE
	}
}