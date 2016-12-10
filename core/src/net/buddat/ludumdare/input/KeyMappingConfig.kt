package net.buddat.ludumdare.input

import com.badlogic.gdx.Input
import java.util.*

/**
 * Configuration for keyboard mapping
 */
data class KeyMappingConfig(val up: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_UP),
							val down: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_DOWN),
							val left: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_LEFT),
							val right: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_RIGHT),
							val jump: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_JUMP)) {
	companion object {
		const val DEFAULT_UP = Input.Keys.A
		const val DEFAULT_DOWN = Input.Keys.S
		const val DEFAULT_LEFT = Input.Keys.A
		const val DEFAULT_RIGHT = Input.Keys.D
		const val DEFAULT_JUMP = Input.Keys.SPACE
	}
}