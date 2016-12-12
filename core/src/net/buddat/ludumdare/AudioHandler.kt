package net.buddat.ludumdare

import java.util.HashMap
import java.util.Random

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.Gdx

import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.FixedBlock
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.effects.CandyEffectType

class AudioHandler: LogicEngine.CandyRemovalListener, LogicEngine.JumpListener, LogicEngine.LandListener {
	
	val folder = "sounds/"
	
	val candy1 = "sfx_blippy1.ogg"
	val candy2 = "sfx_blippy2.ogg"
	val jump1 = "sfx_jump.ogg"
	val run = "sfx_stomps.ogg"
	val swirl1 = "sfx_itsmagic1.ogg"
	val swirl2 = "sfx_itsmagic2.ogg"
	
	var soundList: HashMap<String, Sound> = HashMap<String, Sound>()
	var rand: Random = Random(System.currentTimeMillis())
	
	var sndRunning = false
	
	override fun onCandyRemoval(candy: Candy) {
		var soundName = candy2
		if (candy.candyEffectType != CandyEffectType.NO_EFFECT)
			soundName = candy1
		
		var sound = getSound(soundName)
		if (sound != null)
			sound.play()
	}
	
	override fun onJump(player: PlayerEntity, fromSurface: FixedBlock) {
		var sound = getSound(jump1)
		if (sound != null)
			sound.play()
	}
	
	override fun onLand(player: PlayerEntity, fixedBlock: FixedBlock) {
		
	}
	
	fun playSwirl() {
		var soundName = swirl1
		when(rand.nextBoolean()) {
			true -> soundName = swirl2
		}
		
		var sound = getSound(soundName)
		if (sound != null)
			sound.play()
	}
	
	fun getSound(name: String): Sound? {
		if (!soundList.contains(name))
			soundList.put(name, Gdx.audio.newSound(Gdx.files.internal(folder + name)))
		
		return soundList.get(name)
	}
	
	fun setRunning(state: Boolean) {
		var sound = getSound(run)
		if (sound != null) {
			if (state && !sndRunning)
				sound.loop()
			else if (!state && sndRunning)
				sound.stop()
		}
		sndRunning = state
	}
}
