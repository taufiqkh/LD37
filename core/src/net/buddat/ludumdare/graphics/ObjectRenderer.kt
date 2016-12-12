package net.buddat.ludumdare.graphics

import java.util.ArrayList
import java.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.audio.Sound

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

	lateinit var batch: SpriteBatch
	lateinit var objectList: ArrayList<ObjectRenderable>
	lateinit var candyTex: Texture
	lateinit var sawTex: Texture
	
	lateinit var eatSound: Sound
	lateinit var eatSound2: Sound
	
	var halfX: Float = 0f
	var halfY: Float = 0f
	
	fun create() {
		batch = SpriteBatch()
		objectList = ArrayList<ObjectRenderable>()
		
		candyTex = Texture(Gdx.files.internal("pill.png"))
		
		eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_blippy1.ogg"))
		eatSound2 = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_blippy2.ogg"))
		
		halfX = candyTex.width / 4f
		halfY = candyTex.height / 4f
	}
	
	fun render(cam: Camera) {
		batch.projectionMatrix = cam.combined
		batch.begin()
		for (obj in objectList) {
			if (obj.mapObj.mapObject is RectangleMapObject)
				when {
					obj.type == ObjectType.CANDY -> {
						obj.rotation += obj.rotationSpeed
						obj.scale()
						batch.draw(candyTex, obj.mapObj.mapObject.rectangle.x - halfX, obj.mapObj.mapObject.rectangle.y - halfY, candyTex.width / 2f, candyTex.height / 2f,
								candyTex.width.toFloat(), candyTex.height.toFloat(), obj.scale, obj.scale, obj.rotation, 0, 0, candyTex.width, candyTex.height, false, false)
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
		mapObj.candyEffectType == CandyEffectType.JUMP_HIGHER -> 2f
		mapObj.candyEffectType == CandyEffectType.JUMP_LOWER -> 1f
		else -> 1f
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
