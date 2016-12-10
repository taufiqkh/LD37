package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

class Room(mapFile: String) : Entity() {
	
	val walls: MutableList<Wall> = mutableListOf()
	
	public val tiledMap: TiledMap
	
	init {
		tiledMap = TmxMapLoader().load(mapFile)
		tiledMap.layers.get(1).isVisible = false // Hide the collision layer
	}
	
	public fun getCollisionLayer(): MapLayer {
		return tiledMap.layers.get(1)
	}
	
}