package net.buddat.ludumdare.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.utils.Array as GdxArray

class PlayerViewer {
	
	val spriteSheetCols = 8
	val spriteSheetRows = 4
	
	lateinit var spriteSheet: Texture
	lateinit var runFrames: GdxArray<TextureRegion>
	lateinit var runAnimation: Animation
	lateinit var spriteBatch: SpriteBatch
	lateinit var currentFrame: TextureRegion
	
	var stateTime: Float = 0f
	
	fun create() {
		spriteSheet = Texture(Gdx.files.internal("sprit_grey.png"))
		var tmp = TextureRegion.split(spriteSheet, spriteSheet.width / spriteSheetCols, spriteSheet.height / spriteSheetRows)
		runFrames = GdxArray(spriteSheetCols)
		for (tmpTexRegion in tmp[spriteSheetRows - 1])
			runFrames.add(tmpTexRegion)
		runAnimation = Animation(0.1f, runFrames)
		spriteBatch = SpriteBatch()
	}
	
	fun render(x: Float, y: Float) {
		stateTime += Gdx.graphics.deltaTime
		currentFrame = runAnimation.getKeyFrame(stateTime, true)
		
		spriteBatch.begin()
		spriteBatch.draw(currentFrame, x, y)
		spriteBatch.end()
	}
	
	fun dispose() {
		spriteSheet.dispose()
		spriteBatch.dispose()
	}
	
}
