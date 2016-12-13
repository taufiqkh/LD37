package net.buddat.ludumdare

import java.util.Random

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.Pixmap.Format

import net.buddat.ludumdare.entity.Candy
import net.buddat.ludumdare.entity.effects.CandyEffectType
import net.buddat.ludumdare.graphics.PlayerRenderer
import net.buddat.ludumdare.graphics.AnimationState
import net.buddat.ludumdare.graphics.UIRenderer
import net.buddat.ludumdare.graphics.ObjectRenderer
import net.buddat.ludumdare.graphics.ObjectRenderable
import net.buddat.ludumdare.graphics.ObjectType

class LD37 : ApplicationAdapter(), LogicEngine.CandyRemovalListener{

	private val logic: LogicEngine

	init {
		logic = LogicEngine()
	}

	internal lateinit var tiledMap: TiledMap
	internal lateinit var camera: OrthographicCamera
	
	internal lateinit var backgroundCamera: OrthographicCamera
	internal lateinit var backgroundImage: Texture
	internal lateinit var backgroundSpritebatch: SpriteBatch
	
	internal lateinit var debugRenderer: Box2DDebugRenderer
	
	internal lateinit var tiledMapRenderer: OrthogonalTiledMapRenderer
	internal lateinit var playerRenderer: PlayerRenderer
	internal lateinit var uiRenderer: UIRenderer
	internal lateinit var objectRenderer: ObjectRenderer
	
	internal lateinit var audioHandler: AudioHandler
	internal lateinit var diffMod: DifficultyModifier
	
	internal lateinit var shaderBg: ShaderProgram
	internal lateinit var shaderMap: ShaderProgram
	
	internal lateinit var mapFbo: FrameBuffer
	internal lateinit var fboRegion: TextureRegion
	internal lateinit var fboBatch: SpriteBatch
	
	var rand = Random(System.currentTimeMillis())

	var shouldZoom = false
	var camZoom = 1f
	var zoomDir = true
	
	var currentDifficulty = -1f
	
	var switchingMap = false
	var mapSwitchFirstCall = true;
	var mapSwitchDir = true
	var mapSwitchAngleChange = 0.1f
	var mapSwitchCurrAngle = 0f
	var mapSwitchAngleMax = mapSwitchAngleChange * 30f
	
	var lastDiffChange = System.currentTimeMillis()
	var diffChangeCooldown = 500L;
	
	fun switchMap(newMap: TiledMap, first: Boolean) {
		if (switchingMap || first) {
			if (mapSwitchFirstCall && !first) {
				mapSwitchFirstCall = false
				audioHandler.playSwirl()
			}
			
			if (mapSwitchDir && mapSwitchCurrAngle < mapSwitchAngleMax && !first) {
				mapSwitchCurrAngle += mapSwitchAngleChange
			} else if (mapSwitchDir || first) {
				mapSwitchDir = false
				if (first)
					mapSwitchDir = true
				tiledMap = newMap
				tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)
				tiledMap.layers.get(Constants.collisionsLayer).isVisible = false
				
				val backgroundImg = tiledMap.layers.get(0).name
				backgroundImage = Texture(Gdx.files.internal(backgroundImg))
				
				objectRenderer.objectList.clear()
				for (candy in logic.currentRoom.candies)
					objectRenderer.objectList.add(ObjectRenderable(candy, ObjectType.CANDY, rand))
				
				currentDifficulty += 1f
			} else if (!mapSwitchDir && mapSwitchCurrAngle > 0f) {
				mapSwitchCurrAngle -= mapSwitchAngleChange
			} else {
				mapSwitchDir = true
				mapSwitchFirstCall = true
				switchingMap = false
				logic.player.ignoreMovement = false
				fboBatch.shader = shaderMap
			}
		}
	}
	
	fun updateCameraPosition() {
		camera.position.set(logic.getPlayerPosn().x * Constants.PPM,
				logic.getPlayerPosn().y * Constants.PPM, 0f)
		
		val camLeft = camera.position.x - camera.viewportWidth / 2f
		val camRight = camera.position.x + camera.viewportWidth / 2f
		val camTop = camera.position.y + camera.viewportHeight / 2f
		val camBottom = camera.position.y - camera.viewportHeight / 2f
		
		if (camLeft < 0)
			camera.position.x = camera.viewportWidth / 2f
		if (camRight > tiledMap.properties.get("width") as Int * Constants.PPM)
			camera.position.x = tiledMap.properties.get("width") as Int * Constants.PPM - camera.viewportWidth / 2f
		if (camTop > tiledMap.properties.get("height") as Int * Constants.PPM)
			camera.position.y = tiledMap.properties.get("height") as Int * Constants.PPM - camera.viewportHeight / 2f
		if (camBottom < 0)
			camera.position.y = camera.viewportHeight / 2f
		
		if (currentDifficulty > 15f)
			when {
				zoomDir -> {
					camZoom += 0.0001f * currentDifficulty
					if (camZoom > 1f)
						zoomDir = false
				}
				else -> {
					camZoom -= 0.0001f * currentDifficulty
					if (camZoom < 1f - 0.005 * currentDifficulty)
						zoomDir = true
				} 
			}
		camera.zoom = camZoom
		
		diffMod.applyRotationDifficulty(currentDifficulty, camera)
		
		backgroundCamera.position.x = camera.position.x / Constants.backgroundCameraSpeed
		backgroundCamera.position.y = camera.position.y / Constants.backgroundCameraSpeed
	}

	override fun create() {
		var w: Float = Gdx.graphics.width.toFloat()
		var h: Float = Gdx.graphics.height.toFloat()

		camera = OrthographicCamera()
		camera.setToOrtho(false, w, h)
		camera.update()
		
		backgroundCamera = OrthographicCamera()
		backgroundCamera.setToOrtho(false, w, h)
		backgroundCamera.update()
		
		shaderBg = ShaderProgram(Gdx.files.internal("shaders/testShad0.vert").readString(), Gdx.files.internal("shaders/testShad0.frag").readString())
		backgroundSpritebatch = SpriteBatch()
		backgroundSpritebatch.shader = shaderBg
		
		shaderMap = ShaderProgram(Gdx.files.internal("shaders/testShad0.vert").readString(), Gdx.files.internal("shaders/testShad1.frag").readString())
		println(shaderMap.log)
		
		logic.createNext()
		
		playerRenderer = PlayerRenderer()
		playerRenderer.create()
		
		objectRenderer = ObjectRenderer()
		objectRenderer.create()
		logic.addCandyRemovalListener(objectRenderer)
		
		uiRenderer = UIRenderer()
		uiRenderer.create()
		
		audioHandler = AudioHandler()
		logic.addCandyRemovalListener(audioHandler)
		logic.addJumpListener(audioHandler)
		logic.addLandListener(audioHandler)
		
		mapFbo = FrameBuffer(Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, false)
		fboRegion = TextureRegion(mapFbo.colorBufferTexture, 0, 0, Gdx.graphics.width, Gdx.graphics.height)
		fboRegion.flip(false, true)
		
		fboBatch = SpriteBatch()
		fboBatch.shader = shaderMap
		
		diffMod = DifficultyModifier()

		switchMap(logic.currentRoom.tiledMap, true)

		debugRenderer = Box2DDebugRenderer(true, true, true, false, true, false)
	}

	override fun render() {
		logic.update(Gdx.graphics.deltaTime)
		var inputResult = logic.inputHandler.poll()
		if (System.currentTimeMillis() - lastDiffChange > diffChangeCooldown) {
			if (inputResult.diffup) {
				currentDifficulty++;
				lastDiffChange = System.currentTimeMillis()
			}
			if (inputResult.diffdown) {
				currentDifficulty--;
				lastDiffChange = System.currentTimeMillis()
			}
		}
		
		audioHandler.playMusic(currentDifficulty / 5f)
		
		if (!logic.player.isAirborne && logic.player.isRunning)
			audioHandler.setRunning(true)
		else
			audioHandler.setRunning(false)
		
		if (logic.currentRoom.tiledMap != tiledMap || switchingMap) {
			switchingMap = true
			logic.player.ignoreMovement = true
			switchMap(logic.currentRoom.tiledMap, false)
			if (switchingMap) fboBatch.shader = shaderBg
		}

		mapFbo.begin()
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
	
			updateCameraPosition()
			
			backgroundCamera.update()
			backgroundSpritebatch.projectionMatrix = backgroundCamera.combined
			backgroundSpritebatch.begin()
			diffMod.applyBackgroundDifficulty(currentDifficulty, backgroundSpritebatch.shader)
			backgroundSpritebatch.draw(backgroundImage, -500f, -500f)
			backgroundSpritebatch.end()
			
			camera.update()
			tiledMapRenderer.setView(camera)
			tiledMapRenderer.render()
			
			objectRenderer.render(camera, currentDifficulty)
	
			playerRenderer.spriteBatch.projectionMatrix = camera.combined
			playerRenderer.currentState = when {
				logic.player.isAirborne -> AnimationState.JUMPING
				logic.player.isRunning -> AnimationState.RUNNING
				else -> AnimationState.IDLE
			}
			playerRenderer.render(logic.getPlayerPosn().x * Constants.PPM, logic.getPlayerPosn().y * Constants.PPM, logic.player.movementDirLeft)
			uiRenderer.render(logic.currentRoom.candies.count(), logic.player.isDead, currentDifficulty)
			//debugRenderer.render(logic.world, camera.combined.scale(Constants.PPM, Constants.PPM, 0f))
		mapFbo.end()
		
		fboBatch.begin()
		if (switchingMap)
			fboBatch.shader.setUniformf("angle", mapSwitchCurrAngle)
		else
			diffMod.applyMapDifficulty(currentDifficulty, fboBatch.shader)
		fboBatch.draw(fboRegion, 0f, 0f)
		fboBatch.end()
	}
		
	override fun dispose() {
		tiledMap.dispose()
		playerRenderer.dispose()
	}
	
	override fun onCandyRemoval(candy: Candy) {
		when (candy.candyEffectType) {
			CandyEffectType.NO_EFFECT -> currentDifficulty += 0.05f
			else -> currentDifficulty += 0.2f
		}
	}
}