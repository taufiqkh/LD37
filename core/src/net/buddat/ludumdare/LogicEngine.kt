package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
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
		createFloor(floor)
		//createCircle()
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
			val mCalc = MovementCalculator(delta)
			mCalc.rawMovement(inputHandler.poll(), player)
		}
	}

	fun createPlayerBody(): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(0f, 10f)
		bodyDef.fixedRotation = true
		val bounds = PolygonShape()
		bounds.setAsBox(0.5f, 1f)
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
	fun createCircle() {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(10f, 10f)
		val circle: CircleShape = CircleShape()
		circle.radius = 2f
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = CircleShape()
		fixtureDef.density = 0.5f
		fixtureDef.friction = 0.4f
		fixtureDef.restitution = 0.5f
		val body: Body = world.createBody(bodyDef)
		body.createFixture(fixtureDef)
		circle.dispose()
	}

	fun createFloor(y: Float): Body {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.position.set(Vector2(0f, y))
		val body: Body = world.createBody(bodyDef)
		val floorBox: PolygonShape = PolygonShape()
		floorBox.setAsBox(100f, 8f)
		val fixtureDef: FixtureDef = FixtureDef()
		fixtureDef.shape = floorBox
		fixtureDef.friction = 2f
		body.createFixture(fixtureDef)
		floorBox.dispose()
		return body
	}
}