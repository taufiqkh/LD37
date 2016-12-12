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
							val jump: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_JUMP),
							val restart: KeyMapping = KeyMapping(KeyMappingConfig.DEFAULT_RESTART)) {
	companion object {
		val DEFAULT_UP = Input.Keys.W
		val DEFAULT_UP2 = Input.Keys.UP
		val DEFAULT_DOWN = Input.Keys.S
		val DEFAULT_DOWN2 = Input.Keys.DOWN
		val DEFAULT_LEFT = Input.Keys.A
		val DEFAULT_LEFT2 = Input.Keys.LEFT
		val DEFAULT_RIGHT = Input.Keys.D
		val DEFAULT_RIGHT2 = Input.Keys.RIGHT
		val DEFAULT_JUMP = Input.Keys.SPACE
		val DEFAULT_RESTART = Input.Keys.R
	}
}