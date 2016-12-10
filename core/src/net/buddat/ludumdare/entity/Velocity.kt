package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * Wrapper for velocity
 */
class Velocity(private val velocity: Vector2) : Component {
    val direction: Float get() = velocity.angle()
    val speed: Float get() = velocity.len()
    val speedSq: Float get() = velocity.len2()
}