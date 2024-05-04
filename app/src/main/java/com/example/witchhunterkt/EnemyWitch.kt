package com.example.witchhunterkt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class EnemyWitch(private val context: Context) {
    private var enemyWitch: Bitmap
    var ex = 0
    var ey = 0
    var enemyVelocity = 0
    private val random: Random

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4 // adjust witch size
        enemyWitch = BitmapFactory.decodeResource(context.resources, R.drawable.witch, options)
        random = Random()
        resetEnemyWitch()
    }

    fun getEnemyWitchBitmap(): Bitmap {
        return enemyWitch
    }

    fun getEnemyWitchWidth(): Int {
        return enemyWitch.width
    }

    fun getEnemyWitchHeight(): Int {
        return enemyWitch.height
    }

    private fun resetEnemyWitch() {
        ex = 200 + random.nextInt(400)
        ey = 0
        enemyVelocity = 17 + random.nextInt(10)
    }

}
