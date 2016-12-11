package net.buddat.ludumdare

import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.physics.box2d.*
import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.util.Types

/**
 * Listener for candy collisions
 */
class CandyContactListener(candies: MapObjects) : ContactListener {
	override fun endContact(contact: Contact?) {

	}

	override fun beginContact(contact: Contact?) {
		if (contact == null) return
		val (candy: Candy, player: PlayerEntity) = Types.matchPair<Candy, PlayerEntity>(
				contact.fixtureA, contact.fixtureB) ?: return
		player.startContact(candy)
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
	}

	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
	}
}