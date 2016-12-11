package net.buddat.ludumdare.movement

import com.badlogic.gdx.math.Vector2
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.input.InputHandler
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Test movement calculations
 */
class MovementCalculatorTest {
	var mCalc: MovementCalculator = MovementCalculator()

	@Before
	fun setUp() {
		mCalc = MovementCalculator()
	}
/*
	@Test
	fun applyGravity() {
		assertEquals("Gravity applied", 0.2f, MovementCalculator(1f).applyGravity(10f), 0.001f)
	}

	@Test
	fun rawYMovement() {
		val player = PlayerEntity()
		player.isAirborne = false
		assertEquals("Jumping", Speed.jumpSpeed, mCalc.calcRawYMovement(jumpInput, player))
	}
*/
	val jumpInput = InputHandler.InputResult(false, false, false, false, true)
}
