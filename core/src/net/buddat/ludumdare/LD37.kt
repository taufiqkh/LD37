package net.buddat.ludumdare

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import net.buddat.ludumdare.graphics.PlayerViewer

class LD37 : ApplicationAdapter() {

	private val logic: LogicEngine

	init {
		logic = LogicEngine()
	}

	internal lateinit var tiledMap: TiledMap
	internal lateinit var camera: OrthographicCamera
	internal lateinit var tiledMapRenderer: TiledMapRenderer
	internal lateinit var playerRenderer: PlayerViewer

	var running = false

	var renderX = 0f
	var renderY = 0f
	
	fun switchMap(newMap: TiledMap) {
		tiledMap = newMap
		tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)
		tiledMap.layers.get(1).isVisible = false
	}
	
	fun updateCameraPosition() {
		camera.position.set(logic.getPlayerPosn().x * PPM + width / 5,
				logic.getPlayerPosn().y * PPM, 0f)
		
		val camLeft = camera.position.x - camera.viewportWidth / 2f
		val camRight = camera.position.x + camera.viewportWidth / 2f
		val camTop = camera.position.y + camera.viewportHeight / 2f
		val camBottom = camera.position.y - camera.viewportHeight / 2f
		
		if (camLeft < 0)
			camera.position.x = camera.viewportWidth / 2f
		if (camRight > tiledMap.properties.get("width") as Int * PPM)
			camera.position.x = tiledMap.properties.get("width") as Int * PPM - camera.viewportWidth / 2f
		if (camTop > tiledMap.properties.get("height") as Int * PPM)
			camera.position.y = tiledMap.properties.get("height") as Int * PPM - camera.viewportHeight / 2f
		if (camBottom < 0)
			camera.position.y = camera.viewportHeight / 2f
	}

	override fun create() {
		var w: Float = Gdx.graphics.width.toFloat()
		var h: Float = Gdx.graphics.height.toFloat()

		camera = OrthographicCamera()
		camera.setToOrtho(false, w, h)
		camera.update()

		logic.currentRoom.create()
		switchMap(logic.currentRoom.tiledMap)

		playerRenderer = PlayerViewer()
		playerRenderer.create()
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

		// Late game bullshit
		// camera.rotate(0.3f)
		
		updateCameraPosition()
		
		camera.update()
		tiledMapRenderer.setView(camera)
		tiledMapRenderer.render()

		playerRenderer.spriteBatch.projectionMatrix = camera.combined
		playerRenderer.render(logic.getPlayerPosn().x * 32f, logic.getPlayerPosn().y * 32f, System.currentTimeMillis() % 2000 > 1000)
	}

	override fun dispose() {
		tiledMap.dispose()
		playerRenderer.dispose()
	}
}