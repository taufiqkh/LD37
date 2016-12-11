package net.buddat.ludumdare.entity.effects

/**
 * Candy effects
 */
enum class CandyEffectType {
	JUMP_HIGHER,
	JUMP_LOWER,
	MOVE_FASTER,
	MOVE_SLOWER,
	HIGHER_GRAVITY,
	LOWER_GRAVITY,
	GRAVITY_UP,
	GRAVITY_DOWN,
	GRAVITY_LEFT,
	GRAVITY_RIGHT,
	MIRROR_IMAGE_HORIZONTAL,
	MIRROR_IMAGE_VERTICAL,
	NO_EFFECT;

	companion object {
		val jumpHigherModifier = 1.42857f
		val jumpLowerModifier = 0.70f
		val moveFasterModifier = 2f
		val moveSlowerModifier = 0.5f
	}
}