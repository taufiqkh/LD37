package net.buddat.ludumdare

import java.util.HashMap
import java.util.Random

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.Gdx

import net.buddat.ludumdare.entity.Candy

class AudioHandler: LogicEngine.CandyRemovalListener {
	
	val folder = "sounds/"
	
	val candy1 = "sfx_blippy1.ogg"
	val candy2 = "sfx_blippy2.ogg"
	
	var soundList: HashMap<String, Sound> = HashMap<String, Sound>()
	var rand: Random = Random(System.currentTimeMillis())
	
	override fun onCandyRemoval(candy: Candy) {
		var soundName = candy1
		when(rand.nextBoolean()) {
			true -> soundName = candy2
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
}
