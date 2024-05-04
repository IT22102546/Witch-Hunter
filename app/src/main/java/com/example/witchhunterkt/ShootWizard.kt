package com.example.witchhunterkt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ShootWizard(context: Context, var shx: Int, var shy: Int) {

    var wizardShoot: Bitmap

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        wizardShoot = BitmapFactory.decodeResource(context.resources, R.drawable.wizardshoot,options)
    }

    fun getWizardShootBitmap(): Bitmap {
        return wizardShoot
    }

    fun getShotWidth(): Int {
        return wizardShoot.width
    }

    fun getShotHeight(): Int {
        return wizardShoot.height
    }
}
