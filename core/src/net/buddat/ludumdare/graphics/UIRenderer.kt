package net.buddat.ludumdare.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import net.buddat.ludumdare.Constants

class UIRenderer {
	
	lateinit var fontTexture: Texture
	lateinit var font: BitmapFont
	lateinit var spriteBatch: SpriteBatch
	
	public fun create() {
		fontTexture = Texture(Gdx.files.internal("font.png"))
		fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
		font = BitmapFont(Gdx.files.internal("font.fnt"), TextureRegion(fontTexture), false)
		font.data.scale(-0.65f)
		
		spriteBatch = SpriteBatch()
	}
	
	public fun render() {
		spriteBatch.begin()
		font.draw(spriteBatch, "Holy shit there are words up here", 10f, Constants.height - 10f)
		spriteBatch.end()
	}
}
