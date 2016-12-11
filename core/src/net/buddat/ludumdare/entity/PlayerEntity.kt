package net.buddat.ludumdare.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import net.buddat.ludumdare.entity.effects.CandyEffectType

/**
 * Entity representing the player
 */
class PlayerEntity(val body: Body, val feet: Fixture) : Entity() {
	init {
		body.userData = this
	}

	val candyEffectTypes: MutableSet<CandyEffectType> = mutableSetOf()

	val position: Vector2
		get() = body.position.cpy()

	var isAirborne: Boolean = false

	var isRunning: Boolean = false
		get() {
			return body.linearVelocity.x != 0f
		}
	var movementDirLeft: Boolean = false
	var isDead: Boolean = false

	fun startContact(candy: Candy) {
		if (!candy.isEaten) {
			println("Candyyyyyy")
			candy.isEaten = true
		}
	}

	fun startContact(killer: Killer) {
		println("He ded")
		isDead = true
	}

	val blockContacts: MutableSet<FixedBlock> = mutableSetOf()
	val feetContacts: MutableSet<FixedBlock> = mutableSetOf()

	fun startFeetContact(fixedBlock: FixedBlock) {
		feetContacts.add(fixedBlock)
	}

	fun endFeetContact(fixedBlock: FixedBlock) {
		feetContacts.remove(fixedBlock)
	}

	fun startContact(fixedBlock: FixedBlock) {
		blockContacts.add(fixedBlock)
	}

	fun endContact(fixedBlock: FixedBlock) {
		blockContacts.remove(fixedBlock)
	}

	fun startContact(contactableEntity: ContactableEntity) {
		println("Unknown wtf")
	}
}