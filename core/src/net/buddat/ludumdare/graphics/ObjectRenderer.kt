package net.buddat.ludumdare.graphics

import java.util.ArrayList
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
						batch.draw(candyTex, obj.mapObj.mapObject.rectangle.x - halfX, obj.mapObj.mapObject.rectangle.y - halfY)
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

class ObjectRenderable(val mapObj: Candy, val type: ObjectType) {

}
