package net.buddat.ludumdare.entity.effects

/**
 * Candy effects
 */
enum class CandyEffectType(name: String) {
	JUMP_HIGHER("jumphigher"),
	JUMP_LOWER("jumplower"),
	MOVE_FASTER("movefaster"),
	MOVE_SLOWER("moveslower"),
	HIGHER_GRAVITY("highergravity"),
	LOWER_GRAVITY("lowergravity"),
	GRAVITY_UP("gravityup"),
	GRAVITY_DOWN("gravitydown"),
	GRAVITY_LEFT("gravityleft"),
	GRAVITY_RIGHT("gravityright"),
	MIRROR_IMAGE_HORIZONTAL("mirrorhorz"),
	MIRROR_IMAGE_VERTICAL("mirrorvert"),
	NO_EFFECT("noeffect");

	companion object {
		val jumpHigherModifier = 1.5f
		val jumpLowerModifier = 0.5f
		val moveFasterModifier = 2f
		val moveSlowerModifier = 0.5f
	}
}