package net.buddat.ludumdare.graphics

import java.util.ArrayList
import java.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject

import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.Constants
import net.buddat.ludumdare.LogicEngine
import net.buddat.ludumdare.entity.Room
import net.buddat.ludumdare.entity.effects.CandyEffectType

class ObjectRenderer : LogicEngine.CandyRemovalListener {
	
	override fun onCandyRemoval(candy: Candy) {
		var toRemove: ObjectRenderable? = null
		for (obj in objectList)
			if (obj.type == ObjectType.CANDY)
				if (obj.mapObj == candy)
					toRemove = obj
		
		if (toRemove != null)
			objectList.remove(toRemove)
	}

	val pickupSize: Int = 24
	
	lateinit var batch: SpriteBatch
	lateinit var objectList: ArrayList<ObjectRenderable>
	lateinit var candyTex: Texture
	
	lateinit var pillsSheet: Texture
	lateinit var pills: Array<TextureRegion>
	lateinit var candySheet: Texture
	lateinit var candies: Array<TextureRegion>
	
	var halfX: Float = 0f
	var halfY: Float = 0f
	
	fun create() {
		batch = SpriteBatch()
		objectList = ArrayList<ObjectRenderable>()
		
		pillsSheet = Texture(Gdx.files.internal("pills.png"))
		pills = TextureRegion.split(pillsSheet, pickupSize, pickupSize)[0]
		
		candySheet = Texture(Gdx.files.internal("candies.png"))
		candies = TextureRegion.split(candySheet, pickupSize, pickupSize)[0]
		
		candyTex = Texture(Gdx.files.internal("pill.png"))
		
		halfX = candyTex.width / 4f
		halfY = candyTex.height / 4f
	}

	fun graphicForEffect(diff: Float, effectType: CandyEffectType): TextureRegion {
		val powerUpGraphics = if (diff < 5) pills else candies
		return powerUpGraphics[when (effectType) {
			CandyEffectType.NO_EFFECT -> 0
			CandyEffectType.JUMP_HIGHER -> 1
			CandyEffectType.JUMP_LOWER -> 2
			CandyEffectType.MOVE_FASTER -> 3
			CandyEffectType.MOVE_SLOWER -> 4
			else -> 5
		}]
	}

	fun render(cam: Camera, diff: Float) {
		batch.projectionMatrix = cam.combined
		batch.begin()
		for (obj in objectList) {
			if (obj.mapObj.mapObject is RectangleMapObject)
				when {
					obj.type == ObjectType.CANDY -> {
						val tex: TextureRegion = graphicForEffect(diff, obj.mapObj.candyEffectType)
						obj.rotation += obj.rotationSpeed
						obj.scale()
						batch.draw(tex, obj.mapObj.mapObject.rectangle.x - halfX, obj.mapObj.mapObject.rectangle.y - halfY, tex.regionWidth / 2f, tex.regionHeight / 2f,
								tex.regionWidth.toFloat(), tex.regionHeight.toFloat(), obj.scale, obj.scale, obj.rotation)
					}
					obj.type == ObjectType.SAWBLADE -> {
						
					}
				}
		}
		batch.end()
	}
	
}

enum class ObjectType {
	CANDY, SAWBLADE
}

class ObjectRenderable(val mapObj: Candy, val type: ObjectType, val rand: Random) {
	var rotation = rand.nextFloat() * 360f
	var rotationSpeed = when {
		mapObj.candyEffectType == CandyEffectType.MOVE_FASTER -> 10f + rand.nextFloat() * 10f
		mapObj.candyEffectType == CandyEffectType.MOVE_SLOWER -> 2f - rand.nextFloat()
		mapObj.candyEffectType != CandyEffectType.NO_EFFECT -> 5f + rand.nextFloat() * 5f
		else -> 2f
	}
	
	var scale = 1f
	var scaleMax = when {
		mapObj.candyEffectType == CandyEffectType.NO_EFFECT -> 1f
		else -> 2f
	}
	var scaleMin = when {
		mapObj.candyEffectType == CandyEffectType.JUMP_HIGHER -> 1f
		mapObj.candyEffectType == CandyEffectType.JUMP_LOWER -> 0.5f
		else -> 1f
	}
	var scaleAmnt = 0.01f
	var scaleDir = true
	
	fun scale() {
		when {
			scaleDir -> {
				scale += scaleAmnt
				if (scale > scaleMax)
					scaleDir = false
			}
			!scaleDir -> {
				scale -= scaleAmnt
				if (scale < scaleMin)
					scaleDir = true
			}
		}
		
	}
}
