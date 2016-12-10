package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

public class Room(mapFile: String) : Entity() {
	
	val walls: MutableList<Wall> = mutableListOf()
	var currMapFile: String
	
	init {
		currMapFile = mapFile
		
	}
	
	lateinit var tiledMap: TiledMap
	
	public fun create() {
		tiledMap = TmxMapLoader().load(currMapFile)
		tiledMap.layers.get(1).isVisible = false // Hide the collision layer
	}
	
	public fun getCollisionLayer(): TiledMapTileLayer {
		return tiledMap.layers.get(1) as TiledMapTileLayer
	}
	
}