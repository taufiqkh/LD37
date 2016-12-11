package net.buddat.ludumdare.entity

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import net.buddat.ludumdare.entity.effects.CandyEffectType

/**
 * Candy
 */
data class Candy(val mapObject: RectangleMapObject,
				 val body: Body,
				 val candyEffectType: CandyEffectType) : ContactableEntity {
	var isEaten: Boolean = false
}