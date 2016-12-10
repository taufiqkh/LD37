package net.buddat.ludumdare

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.Input.*
import com.badlogic.gdx.maps.tiled.*
import com.badlogic.gdx.maps.tiled.renderers.*

class LD37 : ApplicationAdapter(), InputProcessor {

	private val logic: LogicEngine
	private val logicThread: Thread

	init {
		logic = LogicEngine()
		logicThread = Thread(logic)
		logicThread.start()
	}
	
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
	internal lateinit var tiledMap: TiledMap
	internal lateinit var camera: OrthographicCamera
	internal lateinit var tiledMapRenderer: TiledMapRenderer
	
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
		Gdx.input.inputProcessor = this
		
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

	override fun render() {
		running = true
		
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		
		camera.update()
		tiledMapRenderer.setView(camera)
		tiledMapRenderer.render()
		
        batch.begin()
        batch.draw(img, renderX, renderY)
        batch.end()
    }
	
    override fun dispose() {
        batch.dispose()
        img.dispose()
		tiledMap.dispose()
    }
	
	override fun keyDown(keycode: Int): Boolean {
        return false;
    }

    override fun keyUp(keycode: Int): Boolean {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32f, 0f);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32f, 0f);
        if(keycode == Input.Keys.UP)
            camera.translate(0f, 32f);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0f, -32f);
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
		
		when {
			keycode == Input.Keys.B -> switchMap("testingMap2.tmx")
		}
		
        return false;
    }

    override fun keyTyped(character: Char): Boolean {
        return false;
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false;
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false;
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false;
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false;
    }

    override fun scrolled(amount: Int): Boolean {
        return false;
    }
}