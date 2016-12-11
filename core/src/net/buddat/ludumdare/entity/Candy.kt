package net.buddat.ludumdare.entity

import com.badlogic.gdx.maps.objects.RectangleMapObject

/**
 * Candy
 */
data class Candy(val mapObject: RectangleMapObject) {
	var isEaten: Boolean = false
}