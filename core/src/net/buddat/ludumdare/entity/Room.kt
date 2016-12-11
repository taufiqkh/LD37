package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import net.buddat.ludumdare.Constants

public class Room(mapFile: String) : Entity() {
	
	val fixedBlocks: MutableList<FixedBlock> = mutableListOf()
	var currMapFile: String
	
	init {
		currMapFile = mapFile
		
	}
	
	lateinit var tiledMap: TiledMap
	
	public fun create() {
		tiledMap = TmxMapLoader().load(currMapFile)
	}
	
	public fun getCollisionLayer(): TiledMapTileLayer {
		return tiledMap.layers.get(Constants.collisionsLayer) as TiledMapTileLayer
	}
	
	public fun getCollisionObjects(): MapObjects {
		return tiledMap.layers.get(Constants.collisionsLayer).objects
	}
	
	public fun getCandyObjects(): MapObjects {
		return tiledMap.layers.get(Constants.candyLayer).objects
	}
	
	public fun getSpawnObjects(): MapObjects {
		return tiledMap.layers.get(Constants.spawnLayer).objects
	}

	val candies: MutableList<Candy> = mutableListOf()
	
}