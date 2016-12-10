package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * Wrapper data class for position
 */
data class Position(private val position: Vector2) : Component {
	val x: Float get() = position.x
	val y: Float get() = position.y
}