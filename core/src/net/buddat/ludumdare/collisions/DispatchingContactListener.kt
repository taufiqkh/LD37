package net.buddat.ludumdare.collisions

import com.badlogic.gdx.physics.box2d.*
import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.Killer
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.util.Types

/**
 * On contact, dispatches to the appropriate channels
 */
class DispatchingContactListener() : ContactListener {
	override fun endContact(contact: Contact?) {

	}

	override fun beginContact(contact: Contact?) {
		if (contact == null) return
		val fixtureA = contact.fixtureA
		val fixtureB = contact.fixtureB
		if (tryCandy(fixtureA, fixtureB)) return
		tryKiller(fixtureA, fixtureB)
	}

	fun tryCandy(fixtureA: Fixture, fixtureB: Fixture): Boolean {
		val candyResult = Types.matchPair<Candy, PlayerEntity>(
				fixtureA, fixtureB)
		if (candyResult != null) {
			val (candy: Candy, player: PlayerEntity) = candyResult
			if (!candy.isEaten) {
				player.startContact(candy)
			}
			return true
		}
		return false
	}

	fun tryKiller(fixtureA: Fixture, fixtureB: Fixture): Boolean {
		val killerResult = Types.matchPair<Killer, PlayerEntity>(
				fixtureA, fixtureB)
		if (killerResult != null) {
			val (killer: Killer, player: PlayerEntity) = killerResult
			player.startContact(killer)
			return true
		}
		return false
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

	}

	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

	}
}