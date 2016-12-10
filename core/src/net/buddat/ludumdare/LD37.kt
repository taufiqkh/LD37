package net.buddat.ludumdare

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx.input as input
import com.badlogic.gdx.Input.*

class LD37 : ApplicationAdapter() {

	private val logic: LogicEngine
	private val logicThread: Thread

	init {
		logic = LogicEngine()
		logicThread = Thread(logic)
		logicThread.start()
	}
	
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
	
	var running = false
	
	var renderX = 0f
	var renderY = 0f
	
	var lastChecked = 0L
	var movementSpeed = 1000 / 500f
	
    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

	override fun render() {
		running = true
		checkInput()
		
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(img, renderX, renderY)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
	
	private fun checkInput() {
		var delta = if (lastChecked == 0L) 0L else System.currentTimeMillis() - lastChecked
		lastChecked = System.currentTimeMillis()
		
		when {
			input.isKeyPressed(Keys.RIGHT) -> renderX += delta / movementSpeed
			input.isKeyPressed(Keys.LEFT) -> renderX -= delta / movementSpeed
			input.isKeyPressed(Keys.UP) -> renderY += delta / movementSpeed
			input.isKeyPressed(Keys.DOWN) -> renderY -= delta / movementSpeed
		}
	}
}