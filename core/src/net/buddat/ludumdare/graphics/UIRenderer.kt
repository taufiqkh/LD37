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
		// font.data.scale(1f)
		
		spriteBatch = SpriteBatch()
	}
	
	public fun render(candy: Int, dead: Boolean, diff: Float) {
		spriteBatch.begin()
		if (diff < 5)
			font.draw(spriteBatch, "pills remaining: ${candy}", 10f, Constants.height - 10f)
		else
			font.draw(spriteBatch, "candy remaining: ${candy}", 10f, Constants.height - 10f)
		font.draw(spriteBatch, "press r to restart", 10f, 30f)
		if (dead)
			font.draw(spriteBatch, "try again", Constants.width / 2f - 120f, Constants.height / 2f - 15f)
		font.draw(spriteBatch, diff.toInt().toString(), Constants.width - 60f, 30f)
		spriteBatch.end()
	}
}
