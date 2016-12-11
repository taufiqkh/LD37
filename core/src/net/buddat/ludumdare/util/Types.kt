package net.buddat.ludumdare.util

import com.badlogic.gdx.physics.box2d.Fixture

/**
 * Type utility functions
 */
object Types {
	/**
	 * Given fixtureA and fixtureB and reified types T and U, checks whether:
	 * - fixtureA is of type T and fixtureB is of type U; OR
	 * - fixtureB is of type T and fixtureA is of type U; OR
	 * - neither of the above holds.
	 * and returns a Pair<T, U> with fixtureA and B in the first or second place depending on their type.
	 * If neither of the first two conditions holds, null is returned.
	 */
	inline fun <reified T, reified U> matchPair(fixtureA: Fixture, fixtureB: Fixture):
			Pair<T, U>? {
		val userDataA = fixtureA.body.userData
		val userDataB = fixtureB.body.userData

		return if (userDataA is T && userDataB is U) {
			Pair(userDataA, userDataB)
		} else if (userDataB is T && userDataA is U) {
			Pair<T, U>(userDataB, userDataA)
		} else null
	}

}