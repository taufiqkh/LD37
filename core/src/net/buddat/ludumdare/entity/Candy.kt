package net.buddat.ludumdare.entity

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body

/**
 * Candy
 */
data class Candy(val mapObject: RectangleMapObject, val body: Body) {
	var isEaten: Boolean = false
}