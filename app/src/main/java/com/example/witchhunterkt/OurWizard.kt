package com.example.witchhunterkt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class OurWizard(private val context: Context, private val screenWidth: Int, private val screenHeight: Int) {

    var ourWizard: Bitmap
    var ox = 0
    var oy = 0
    var isAlive = true
    var wizardVelocity = 0
    private val random: Random

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 3 // adjust wizard size
        ourWizard = BitmapFactory.decodeResource(context.resources, R.drawable.wizard, options)
        random = Random()
        resetOurWizard()
    }

    fun getOurWizardBitmap(): Bitmap {
        return ourWizard
    }

    fun getOurWizardWidth(): Int {
        return ourWizard.width
    }

    fun getOurWizardHeight(): Int {
        return ourWizard.height
    }

    private fun resetOurWizard() {
        ox = random.nextInt(screenWidth)
        oy = screenHeight - ourWizard.height
        wizardVelocity = 10 + random.nextInt(6)
    }
}
