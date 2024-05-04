package com.example.witchhunterkt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ShootWitch(context: Context, var shx: Int, var shy: Int) {

    var witchShoot: Bitmap

    init {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4 // adjust the shooing ball size
        witchShoot = BitmapFactory.decodeResource(context.resources, R.drawable.witchshoot1,options)
    }

    fun getWitchShootBitmap(): Bitmap {
        return witchShoot
    }

    fun getShotWidth(): Int {
        return witchShoot.width
    }

    fun getShotHeight(): Int {
        return witchShoot.height
    }
}
