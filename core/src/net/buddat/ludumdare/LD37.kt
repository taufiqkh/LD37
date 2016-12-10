package net.buddat.ludumdare

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.renderers.*
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

		camera.update()
		tiledMapRenderer.setView(camera)
		tiledMapRenderer.render()

		playerRenderer.render(logic.getPlayerPosn().x * 32f, logic.getPlayerPosn().y * 32f, System.currentTimeMillis() % 2000 > 1000)
	}

	override fun dispose() {
		tiledMap.dispose()
		playerRenderer.dispose()
	}
}