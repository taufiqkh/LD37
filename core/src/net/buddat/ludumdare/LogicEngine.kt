package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.maps.objects.RectangleMapObject

import net.buddat.ludumdare.entity.PlayerEntity
import net.buddat.ludumdare.entity.Room
import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.MovementCalculator
import net.buddat.ludumdare.movement.Speed

/**
 * Powers the entity handling
 */
class LogicEngine {
	val engine: Engine = Engine()
	val player: PlayerEntity
	val inputHandler = InputHandler()

	val world: World = World(Vector2(0f, -Speed.gravity), true)

	var currentRoom: Room

	val floor = 0f
	val ceiling = 1000f
	val leftBounds = 0f
	val rightBounds = 1000f

	init {
		currentRoom = Room(Constants.defaultMap)
		player = PlayerEntity(createPlayerBody())
		engine.addEntity(currentRoom)
		engine.addEntity(player)
		//createFloor(floor)
		//createCircle()
	}
	
	fun create() {
		currentRoom.create()
		
		for (mapObject in currentRoom.getCollisionObjects()) {
			if (mapObject is RectangleMapObject) {
				val bodyDef: BodyDef = BodyDef()
				val xPos = mapObject.rectangle.x / Constants.PPM + mapObject.rectangle.width / Constants.PPM / 2f
				val yPos = mapObject.rectangle.y / Constants.PPM + mapObject.rectangle.height / Constants.PPM / 2f
				bodyDef.position.set(Vector2(xPos, yPos))
				val body: Body = world.createBody(bodyDef)
				val floorBox: PolygonShape = PolygonShape()
				floorBox.setAsBox(mapObject.rectangle.width / Constants.PPM / 2f, mapObject.rectangle.height / Constants.PPM / 2f)
				val fixtureDef: FixtureDef = FixtureDef()
				fixtureDef.shape = floorBox
				fixtureDef.friction = 2f
				body.createFixture(fixtureDef)
				floorBox.dispose()
			}
		}
	}

	fun getPlayerPosn(): Vector2 {
		return player.position
	}

	// Avoid slow spiral of death on slow devices
	val minFrameTime = 0.25f
	val timeStep = 1/90.0f
	val velocityIterations = 6
	val positionIterations = 2

	var accumulator:Double = 0.0

	fun update(delta: Float) {
		val frameTime = Math.min(delta, minFrameTime)
		accumulator += frameTime
		while (accumulator >= timeStep) {
			world.step(timeStep, velocityIterations, positionIterations)
			accumulator -= timeStep
			val mCalc = MovementCalculator()
			mCalc.rawMovement(inputHandler.poll(), player)
		}
	}

	fun createPlayerBody(): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(2f, 6f)
		bodyDef.fixedRotation = true
		val body: Body = world.createBody(bodyDef)

		// main body
		val bodyW = 0.4f
		val bodyH = 0.8f
		val bounds = PolygonShape()
		bounds.setAsBox(0.4f, 0.8f)
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = bounds
		fixtureDef.density = 1f
		fixtureDef.friction = 0f
		fixtureDef.restitution = 0f
		body.createFixture(fixtureDef)

		// feet
		val feetBounds = PolygonShape()
		val feetHalfWTop = 0.38f
		val feetHalfWBottom = 0.3f
		val feetHeight = 0.05f
		feetBounds.set(arrayOf(
				Vector2(-feetHalfWTop, -bodyH),
				Vector2(feetHalfWTop, -bodyH),
				Vector2(feetHalfWBottom, -bodyH - feetHeight),
				Vector2(-feetHalfWBottom,-bodyH - feetHeight)))
		//feetBounds.setAsBox(0.38f, 0.05f, Vector2(0f, -bodyH), 0f)
		val feetFixtureDef: FixtureDef = FixtureDef()
		feetFixtureDef.shape = feetBounds
		feetFixtureDef.density = 1f
		feetFixtureDef.friction = 3f
		feetFixtureDef.restitution = 0f
		body.createFixture(feetFixtureDef)

		bounds.dispose()
		feetBounds.dispose()
		return body
	}

	fun createPlayerFeetBody(): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(2f, 6f)
		bodyDef.fixedRotation = true
		val bounds = PolygonShape()
		bounds.setAsBox(0.4f, 0.8f)
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = bounds
		fixtureDef.density = 1f
		fixtureDef.friction = 3f
		fixtureDef.restitution = 0f
		val body: Body = world.createBody(bodyDef)
		body.createFixture(fixtureDef)
		bounds.dispose()
		return body
	}
}