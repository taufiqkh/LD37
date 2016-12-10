package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity

class Room : Entity() {
	val walls: MutableList<Wall> = mutableListOf()
	
}