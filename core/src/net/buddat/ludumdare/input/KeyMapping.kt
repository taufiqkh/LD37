package net.buddat.ludumdare.input

/**
 * Enforce at least one key, with others allowed
 */
class KeyMapping(key: Int, vararg optional: Int) {
	val keys: Set<Int> = setOf(key).union(optional.toSet())
}