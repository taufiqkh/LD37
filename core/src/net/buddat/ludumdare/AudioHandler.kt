package net.buddat.ludumdare

import java.util.HashMap
import java.util.Random

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.Gdx

import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.FixedBlock
import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.effects.CandyEffectType

class AudioHandler: LogicEngine.CandyRemovalListener, LogicEngine.JumpListener, LogicEngine.LandListener, Music.OnCompletionListener {
	
	val folder = "sounds/"
	
	val candy1 = "sfx_blippy1.ogg"
	val candy2 = "sfx_blippy2.ogg"
	val jump1 = "sfx_jump.ogg"
	val run = "sfx_stomps.ogg"
	val swirl1 = "sfx_itsmagic1.ogg"
	val swirl2 = "sfx_itsmagic2.ogg"
	
	val music1 = "bg_start1.ogg"
	val music2 = "bg_start2.ogg"
	val music3 = "bg_start3.ogg"
	val music1loop = "bg_loop1.ogg"
	val music2loop = "bg_loop2.ogg"
	val music3loop = "bg_loop3.ogg"
	
	var music1Looping = false
	var music2Looping = false
	var music3Looping = false
	
	var soundList: HashMap<String, Sound> = HashMap<String, Sound>()
	var musicList: HashMap<String, Music> = HashMap<String, Music>()
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
	
	override fun onCompletion(music: Music) {
		when {
			music == getMusic(music1) -> music1Looping = true
			music == getMusic(music2) -> music2Looping = true
			music == getMusic(music3) -> music3Looping = true
		}
		
		println(music1Looping)
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
	
	fun playMusic(num: Float) {
		var currentMusic = getMusic(when (num.toInt()) {
			0 -> if (music1Looping) music1loop else music1
			1 -> if (music2Looping) music2loop else music2
			else -> if (music3Looping) music3loop else music3
		})
		
		if (currentMusic != null && !currentMusic.isPlaying) {
			currentMusic.volume = 0.5f
			currentMusic.play()
			currentMusic.setOnCompletionListener(this)
			when (num.toInt()) {
				0 -> if (music1Looping) currentMusic.isLooping = true
				1 -> if (music2Looping) currentMusic.isLooping = true
				2 -> if (music3Looping) currentMusic.isLooping = true
			}
		}
	}
	
	fun getSound(name: String): Sound? {
		if (!soundList.contains(name))
			soundList.put(name, Gdx.audio.newSound(Gdx.files.internal(folder + name)))
		
		return soundList.get(name)
	}
	
	fun getMusic(name: String): Music? {
		if (!musicList.contains(name))
			musicList.put(name, Gdx.audio.newMusic(Gdx.files.internal(folder + name)))
		
		return musicList.get(name)
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
