package net.buddat.ludumdare

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.utils.Array as GdxArray
import net.buddat.ludumdare.collisions.DispatchingContactListener
import net.buddat.ludumdare.entity.*
import net.buddat.ludumdare.entity.effects.CandyEffectType

import net.buddat.ludumdare.input.InputHandler
import net.buddat.ludumdare.movement.MovementCalculator
import net.buddat.ludumdare.movement.Speed

/**
 * Powers the entity handling
 */
class LogicEngine {
	val engine: Engine = Engine()
	lateinit var player: PlayerEntity
	val inputHandler = InputHandler()

	lateinit var world: World

	lateinit var currentRoom: Room

	val rooms: MutableList<String> = mutableListOf(
			Constants.defaultMap,
			"level1.tmx"
	)

	lateinit var nextMap: String

	//Event Listeners
	private val candyRemovalListeners: MutableList<CandyRemovalListener> = mutableListOf()

	private val playerDeathListeners: MutableList<PlayerDeathListener> = mutableListOf()

	private val jumpListeners: MutableList<JumpListener> = mutableListOf()

	private val landListeners: MutableList<LandListener> = mutableListOf()

	var finished = false

	fun toWorldCoords(coord: Float): Float {
		return coord / Constants.PPM
	}

	fun calcXPos(mapObject: RectangleMapObject): Float {
		return toWorldCoords(mapObject.rectangle.x + mapObject.rectangle.width / 2f)
	}

	fun calcYPos(mapObject: RectangleMapObject): Float {
		return toWorldCoords(mapObject.rectangle.y + mapObject.rectangle.height / 2f)
	}

	fun calcHalfWidth(mapObject: RectangleMapObject): Float {
		return toWorldCoords(mapObject.rectangle.width / 2f)
	}

	fun calcHalfHeight(mapObject: RectangleMapObject): Float {
		return toWorldCoords(mapObject.rectangle.height / 2f)
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
		nextMap = rooms.first()
		rooms.removeAt(0)
		currentRoom = Room(nextMap)
		currentRoom.create()

		world = World(Vector2(0f, -Speed.gravity), true)
		val spawn = currentRoom.getSpawnObjects().first() as RectangleMapObject
		player = createPlayer(toWorldCoords(spawn.rectangle.x), toWorldCoords(spawn.rectangle.y))

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
				body.userData = FixedBlock(body)
				floorBox.dispose()
			}
		}
		currentRoom.getCandyObjects()
				.filterIsInstance<RectangleMapObject>()
				.forEach {
					val body = createBoxSensor(it)
					val candy = Candy(it, body, CandyEffectType.JUMP_HIGHER)
					body.userData = candy
					currentRoom.candies.add(candy)
				}
		world.setContactListener(DispatchingContactListener())
	}

	fun getPlayerPosn(): Vector2 {
		return player.position
	}

	// Avoid slow spiral of death on slow devices
	val minFrameTime = 0.25f
	val timeStep = 1 / 90.0f
	val velocityIterations = 6
	val positionIterations = 2

	var accumulator: Double = 0.0

	private var timeOfDeath = Double.NaN

	fun update(delta: Float) {
		val frameTime = Math.min(delta, minFrameTime)
		accumulator += frameTime
		while (accumulator >= timeStep) {
			world.step(timeStep, velocityIterations, positionIterations)
			accumulator -= timeStep
			val eatenCandies = currentRoom.candies.filter { it.isEaten }
			eatenCandies.forEach {
				currentRoom.candies.remove(it)
				world.destroyBody(it.body)
				player.candyEffectTypes.add(it.candyEffectType)
				for (listener in candyRemovalListeners) {
					listener.onCandyRemoval(it)
				}
			}
			val mCalc = MovementCalculator()
			val (jumpedFrom, landedOn) = mCalc.rawMovement(inputHandler.poll(), player)
			if (player.isDead) {
				if (timeOfDeath.isNaN()) {
					timeOfDeath = 0.0
					for (listener in playerDeathListeners) {
						listener.onPlayerDeath(player)
					}
				} else {
					timeOfDeath += delta
				}
				return
			} else if (jumpedFrom != null) {
				for (listener in jumpListeners) {
					listener.onJump(player, jumpedFrom)
				}
			} else if (landedOn != null) {
				for (listener in landListeners) {
					listener.onLand(player, landedOn)
				}
			}
			if (currentRoom.candies.isEmpty()) {
				if (!rooms.isEmpty()) {
					clearRoom()
					create()
				} else {
					finished = true
				}
			}
		}
	}

	fun clearRoom() {
		player.blockContacts.clear()
		player.feetContacts.clear()
		player.isAirborne = false
		player.isRunning = false
		player.body.angularVelocity = 0f
		player.body.linearVelocity.set(0f, 0f)
		val bodies = GdxArray<Body>(world.bodyCount)
		world.getBodies(bodies)
		for (body in bodies) {
			world.destroyBody(body)
		}
		world.dispose()
	}

	fun createPlayer(x: Float, y: Float): PlayerEntity {
		val bodyDef: BodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.DynamicBody
		bodyDef.position.set(x, y)
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
				Vector2(-feetHalfWBottom, -bodyHH - feetHeight)))
		//feetBounds.setAsBox(0.38f, 0.05f, Vector2(0f, -bodyH), 0f)
		val feetFixtureDef: FixtureDef = FixtureDef()
		feetFixtureDef.shape = feetBounds
		feetFixtureDef.density = 1f
		feetFixtureDef.friction = 3f
		feetFixtureDef.restitution = 0f
		val feet = body.createFixture(feetFixtureDef)

		bounds.dispose()
		feetBounds.dispose()
		return PlayerEntity(body, feet)
	}

	fun addCandyRemovalListener(candyRemovalListener: CandyRemovalListener) {
		candyRemovalListeners.add(candyRemovalListener)
	}

	fun addPlayerDeathListener(playerDeathListener: PlayerDeathListener) {
		playerDeathListeners.add(playerDeathListener)
	}

	fun addJumpListener(jumpListener: JumpListener) {
		jumpListeners.add(jumpListener)
	}

	fun addLandListener(landListener: LandListener) {
		landListeners.add(landListener)
	}

	interface JumpListener {
		fun onJump(player: PlayerEntity, fromSurface: FixedBlock)
	}

	interface LandListener {
		fun onLand(player: PlayerEntity, fixedBlock: FixedBlock)
	}

	interface CandyRemovalListener {
		fun onCandyRemoval(candy: Candy)
	}

	interface PlayerDeathListener {
		fun onPlayerDeath(player: PlayerEntity)
	}
}
