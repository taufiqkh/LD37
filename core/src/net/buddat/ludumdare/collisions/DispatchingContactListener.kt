package net.buddat.ludumdare.collisions

import com.badlogic.gdx.physics.box2d.*
import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.ContactableEntity
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
		val (obj, player) =
				Types.matchPair<ContactableEntity, PlayerEntity>(fixtureA, fixtureB)?: return
		when(obj) {
			is Candy -> player.startContact(obj as Candy)
			is Killer -> player.startContact(obj as Killer)
			else -> player.startContact(obj)
		}
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

	}

	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

	}
}