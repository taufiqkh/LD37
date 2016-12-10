package net.buddat.ludumdare.entity

import com.badlogic.gdx.math.Vector2

/**
 * Representation of a wall. Walls may be horizontal or vertical
 */
class Wall(val orientation: Orientation, val axis: Float) {
	enum class Orientation {
		HORIZONTAL,
		VERTICAL
	}
}