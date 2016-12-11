package net.buddat.ludumdare

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.OrthographicCamera

class DifficultyModifier {
	
	var baseAngle = 0f
	var angleMax = 0.01f
	var angleSpeed = 0.0001f
	var angleDirection = true;
	var currAngle = 0f;
	
	var baseSpeed = 0f
	var speedMax = 0.05f
	var baseDistance = 0f
	var distanceMax = 0.001f
	var startTime = System.currentTimeMillis()
	
	var camRotate = 0.001f
	var totalRotation = 0f
	
	fun applyBackgroundDifficulty(diff: Float, shader: ShaderProgram) {
		println(diff)
		when {
			diff <= 1.0f -> shader.setUniformf("angle", baseAngle)
			diff > 1.0f && diff < 5.0f -> {
				if (angleDirection)
					currAngle += (angleSpeed / 2f) * diff
				else
					currAngle -= (angleSpeed / 2f) * diff
				
				if (Math.abs(currAngle) > angleMax / 2f * diff)
					angleDirection = !angleDirection
				
				shader.setUniformf("angle", currAngle)
			}
			else -> {
				if (angleDirection)
					currAngle += angleSpeed * diff
				else
					currAngle -= angleSpeed * diff
				
				if (Math.abs(currAngle) > angleMax * diff)
					angleDirection = !angleDirection
				
				shader.setUniformf("angle", currAngle)
			}
		}
	}
	
	fun applyMapDifficulty(diff: Float, shader: ShaderProgram) {
		when {
			diff < 5.0f -> {
				shader.setUniformf("speed", baseSpeed)
				shader.setUniformf("distance", baseDistance)
				shader.setUniformf("time", (System.currentTimeMillis() - startTime) / 1000f)
			}
			else -> {
				shader.setUniformf("speed", speedMax * diff / 2f)
				shader.setUniformf("distance", distanceMax * diff / 2f)
				shader.setUniformf("time", (System.currentTimeMillis() - startTime) / 1000f)
			}
		}
	}
	
	fun applyRotationDifficulty(diff: Float, camera: OrthographicCamera) {
		when {
			diff > 10.0f -> {
				totalRotation += camRotate * diff / 5f
				camera.rotate(camRotate * diff / 5f)
			}
			totalRotation > 0 -> {
				camera.rotate(-totalRotation)
				totalRotation = 0f
			}
		}
	}
}