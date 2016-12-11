package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.maps.objects.RectangleMapObject
import net.buddat.ludumdare.collisions.CandyContactListener
import net.buddat.ludumdare.entity.Candy

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

	init {
		currentRoom = Room(Constants.defaultMap)
		player = PlayerEntity(createPlayerBody())
		engine.addEntity(currentRoom)
		engine.addEntity(player)
	}

	fun calcXPos(mapObject: RectangleMapObject): Float {
		return mapObject.rectangle.x / Constants.PPM + mapObject.rectangle.width / Constants.PPM / 2f
	}

	fun calcYPos(mapObject: RectangleMapObject): Float {
		return mapObject.rectangle.y / Constants.PPM + mapObject.rectangle.height / Constants.PPM / 2f
	}

	fun calcHalfWidth(mapObject: RectangleMapObject): Float {
		return mapObject.rectangle.width / Constants.PPM / 2f
	}

	fun calcHalfHeight(mapObject: RectangleMapObject): Float {
		return mapObject.rectangle.height / Constants.PPM / 2f
	}

	fun createBoxSensor(mapObject: RectangleMapObject): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.position.set(Vector2(calcXPos(mapObject), calcYPos(mapObject)))
		val body: Body = world.createBody(bodyDef)
		val box: PolygonShape = PolygonShape()
		box.setAsBox(calcHalfWidth(mapObject), calcHalfHeight(mapObject))
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = box
		fixtureDef.isSensor = true
		body.createFixture(fixtureDef)
		box.dispose()
		return body
	}

	fun create() {
		currentRoom.create()
		
		for (mapObject in currentRoom.getCollisionObjects()) {
			if (mapObject is RectangleMapObject) {
				val bodyDef: BodyDef = BodyDef()
				bodyDef.position.set(Vector2(calcXPos(mapObject), calcYPos(mapObject)))
				val body: Body = world.createBody(bodyDef)
				val floorBox: PolygonShape = PolygonShape()
				floorBox.setAsBox(calcHalfWidth(mapObject), calcHalfHeight(mapObject))
				val fixtureDef: FixtureDef = FixtureDef()
				fixtureDef.shape = floorBox
				fixtureDef.friction = 2f
				body.createFixture(fixtureDef)
				floorBox.dispose()
			}
		}
		currentRoom.getCandyObjects()
				.filterIsInstance<RectangleMapObject>()
				.forEach {
					val body = createBoxSensor(it)
					val candy = Candy(it, body)
					body.userData = candy
					currentRoom.candies.add(candy)
				}
		world.setContactListener(CandyContactListener())

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
			val eatenCandies = currentRoom.candies.filter { it.isEaten }
			eatenCandies.forEach {
				currentRoom.candies.remove(it)
				world.destroyBody(it.body)
			}
		}
	}

	fun createPlayerBody(): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(2f, 6f)
		bodyDef.fixedRotation = true
		val body: Body = world.createBody(bodyDef)

		// main body
		val bodyHW = 0.4f
		val bodyHH = 0.8f
		val bounds = PolygonShape()
		bounds.setAsBox(bodyHW, bodyHH)
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = bounds
		fixtureDef.density = 1f
		fixtureDef.friction = 0f
		fixtureDef.restitution = 0f
		body.createFixture(fixtureDef)

		// feet are set to a trapezoid at the bottom of the main body
		val feetBounds = PolygonShape()
		val feetHalfWTop = bodyHW - 0.02f
		val feetHalfWBottom = feetHalfWTop - 0.08f
		val feetHeight = 0.05f
		feetBounds.set(arrayOf(
				Vector2(-feetHalfWTop, -bodyHH),
				Vector2(feetHalfWTop, -bodyHH),
				Vector2(feetHalfWBottom, -bodyHH - feetHeight),
				Vector2(-feetHalfWBottom,-bodyHH - feetHeight)))
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
}