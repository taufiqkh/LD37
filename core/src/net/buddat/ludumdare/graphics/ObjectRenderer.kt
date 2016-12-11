package net.buddat.ludumdare.graphics

import java.util.ArrayList
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.utils.Array as GdxArray

import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.Constants
import net.buddat.ludumdare.LogicEngine
import net.buddat.ludumdare.entity.Room

class ObjectRenderer : LogicEngine.CandyRemovalListener {
	override fun onCandyRemoval(candy: Candy) {
		print("omnomnom")
	}

	lateinit var batch: SpriteBatch
	lateinit var objectList: ArrayList<ObjectRenderable>
	lateinit var candyTex: Texture
	lateinit var sawTex: Texture
	
	fun create() {
		batch = SpriteBatch()
		objectList = ArrayList<ObjectRenderable>()
		
		candyTex = Texture(Gdx.files.internal("pill.png"))
	}
	
	fun checkCandy(currentRoom: Room) {
		objectList.clear()
		for (candy in currentRoom.candies)
			objectList.add(ObjectRenderable(candy, ObjectType.CANDY))	
	}
	
	fun render(cam: Camera) {
		batch.projectionMatrix = cam.combined
		batch.begin()
		for (obj in objectList) {
			if (obj.mapObj.mapObject is RectangleMapObject)
				when {
					obj.type == ObjectType.CANDY -> {
						batch.draw(candyTex, obj.mapObj.mapObject.rectangle.x, obj.mapObj.mapObject.rectangle.y)
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
