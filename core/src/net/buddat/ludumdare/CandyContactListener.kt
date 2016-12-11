package net.buddat.ludumdare

import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.physics.box2d.*
import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.PlayerEntity

/**
 * Listener for candy collisions
 */
class CandyContactListener(candies: MapObjects) : ContactListener {
	override fun endContact(contact: Contact?) {

	}

	inline fun <reified A, reified B> match(fixtureA: Fixture, fixtureB: Fixture):
			Pair<A, B>? {
		val userDataA = fixtureA.body.userData
		val userDataB = fixtureB.body.userData

		return if (userDataA is A && userDataB is B) {
			Pair(userDataA, userDataB)
		} else if (userDataB is A && userDataA is B) {
			Pair<A, B>(userDataB, userDataA)
		} else null
	}

	override fun beginContact(contact: Contact?) {
		if (contact == null) return
		val result = match<Candy, PlayerEntity>(contact.fixtureA, contact.fixtureB)
		if (result == null) return
		val (candy: Candy, player: PlayerEntity) = result
		player.startContact(candy)
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
	}

	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
	}
}