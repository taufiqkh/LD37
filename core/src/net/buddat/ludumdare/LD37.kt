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

	fun switchMap(newMap: String) {
		tiledMap = TmxMapLoader().load(newMap)
		tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)
		tiledMap.layers.get(1).isVisible = false
	}

	override fun create() {
		var w: Float = Gdx.graphics.width.toFloat()
		var h: Float = Gdx.graphics.height.toFloat()

		camera = OrthographicCamera()
		camera.setToOrtho(false, w, h)
		camera.update()

		switchMap("testingMap.tmx")

		playerRenderer = PlayerViewer()
		playerRenderer.create()
	}

	override fun render() {
		running = true
		logic.update(Gdx.graphics.deltaTime)

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		camera.update()
		tiledMapRenderer.setView(camera)
		tiledMapRenderer.render()

		playerRenderer.render(400f, 300f)
	}

	override fun dispose() {
		tiledMap.dispose()
		playerRenderer.dispose()
	}
}