package net.buddat.ludumdare.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.utils.Array as GdxArray

enum class AnimationState {
	RUNNING, IDLE, JUMPING, LANDING
}

class PlayerViewer {
	
	val spriteSheetCols = 8
	val spriteSheetRows = 4
	
	var currentState: AnimationState = AnimationState.IDLE
	
	lateinit var spriteSheet: Texture
	
	lateinit var runFrames: GdxArray<TextureRegion>
	lateinit var runAnimation: Animation
	
	lateinit var idleAnimation: Animation
	
	lateinit var jumpFrames: GdxArray<TextureRegion>
	lateinit var jumpAnimation: Animation
	
	lateinit var landingFrames: GdxArray<TextureRegion>
	lateinit var landingAnimation: Animation
	
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
		
		idleAnimation = Animation(0.1f, tmp[0][0])
		
		jumpAnimation = Animation(0.1f, tmp[1][5])
		
		landingAnimation = Animation(0.1f, tmp[0][7])
		
		spriteBatch = SpriteBatch()
	}
	
	fun render(x: Float, y: Float) {
		stateTime += Gdx.graphics.deltaTime
		
		currentFrame = when(currentState) {
			AnimationState.IDLE -> idleAnimation.getKeyFrame(stateTime, true)
			AnimationState.RUNNING -> runAnimation.getKeyFrame(stateTime, true)
			AnimationState.JUMPING -> jumpAnimation.getKeyFrame(stateTime, true)
			else -> landingAnimation.getKeyFrame(stateTime, true)
		}
		
		spriteBatch.begin()
		spriteBatch.draw(currentFrame, x, y)
		spriteBatch.end()
	}
	
	fun dispose() {
		spriteSheet.dispose()
		spriteBatch.dispose()
	}
	
}
