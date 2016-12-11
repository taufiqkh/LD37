package net.buddat.ludumdare.collisions

import com.badlogic.gdx.physics.box2d.*
import net.buddat.ludumdare.entity.*
import net.buddat.ludumdare.util.Types

/**
 * On contact, dispatches to the appropriate channels
 */
class DispatchingContactListener() : ContactListener {
	override fun endContact(contact: Contact?) {
		if (contact == null) return
		val fixtureA = contact.fixtureA
		val fixtureB = contact.fixtureB
		val (obj, player) =
				Types.matchPair<ContactableEntity, PlayerEntity>(fixtureA, fixtureB)?: return
		when (obj) {
			is FixedBlock -> {
				if (player.feet == fixtureA || player.feet == fixtureB) player.endFeetContact(obj)
				else player.endContact(obj as FixedBlock)
			}
		}
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
			is FixedBlock -> {
				if (player.feet == fixtureA || player.feet == fixtureB) player.startFeetContact(obj)
				else player.startContact(obj as FixedBlock)
			}
			else -> player.startContact(obj)
		}
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

	}

	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

	}
}