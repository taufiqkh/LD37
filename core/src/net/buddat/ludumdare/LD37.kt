package net.buddat.ludumdare

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer

import net.buddat.ludumdare.graphics.PlayerRenderer
import net.buddat.ludumdare.graphics.UIRenderer

class LD37 : ApplicationAdapter() {

	private val logic: LogicEngine

	init {
		logic = LogicEngine()
	}

	internal lateinit var tiledMap: TiledMap
	internal lateinit var camera: OrthographicCamera
	internal lateinit var debugRenderer: Box2DDebugRenderer
	
	internal lateinit var tiledMapRenderer: TiledMapRenderer
	internal lateinit var playerRenderer: PlayerRenderer
	internal lateinit var uiRenderer: UIRenderer

	var running = false

	var renderX = 0f
	var renderY = 0f
	
	var shouldZoom = false
	var camZoom = 1f;
	var zoomDir = true;
	
	var shouldRotate = false
	var camRotate = 0.15f
	
	fun switchMap(newMap: TiledMap) {
		tiledMap = newMap
		tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)
		tiledMap.layers.get(Constants.collisionsLayer).isVisible = false
	}
	
	fun updateCameraPosition() {
		camera.position.set(logic.getPlayerPosn().x * Constants.PPM + Constants.width / 5,
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
		
		if (shouldZoom)
			when {
				zoomDir -> {
					camZoom += 0.0001f
					if (camZoom > 1f)
						zoomDir = false
				}
				else -> {
					camZoom -= 0.0001f
					if (camZoom < 0.95f)
						zoomDir = true
				} 
			}
		camera.zoom = camZoom
		
		if (shouldRotate)
			camera.rotate(camRotate)
	}

	override fun create() {
		var w: Float = Gdx.graphics.width.toFloat()
		var h: Float = Gdx.graphics.height.toFloat()

		camera = OrthographicCamera()
		camera.setToOrtho(false, w, h)
		camera.update()

		logic.currentRoom.create()
		switchMap(logic.currentRoom.tiledMap)

		playerRenderer = PlayerRenderer()
		playerRenderer.create()
		
		uiRenderer = UIRenderer()
		uiRenderer.create()
		
		debugRenderer = Box2DDebugRenderer(true, true, true, false, true, false)
	}

	override fun render() {
		running = true
		logic.update(Gdx.graphics.deltaTime)
		
		if (logic.currentRoom.tiledMap != tiledMap) {
			// TODO: Morph between current map and new map
			switchMap(logic.currentRoom.tiledMap)
		}

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		updateCameraPosition()
		
		camera.update()
		tiledMapRenderer.setView(camera)
		tiledMapRenderer.render()

		playerRenderer.spriteBatch.projectionMatrix = camera.combined
		playerRenderer.render(logic.getPlayerPosn().x * Constants.PPM, logic.getPlayerPosn().y * Constants.PPM, System.currentTimeMillis() % 2000 > 1000)
		
		debugRenderer.render(logic.world, camera.combined.scale(Constants.PPM, Constants.PPM, 0f))
		
		uiRenderer.render()
	}

	override fun dispose() {
		tiledMap.dispose()
		playerRenderer.dispose()
	}
}