package com.example.witchhunterkt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Explotion(context: Context, initialX: Int, initialY: Int) {
    private val explosion = arrayOfNulls<Bitmap>(9)
    var explosionFrame = 0
    var eX = 0
    var eY = 0

    init {
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion0)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
        explosion[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion4)
        explosion[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion5)
        explosion[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion6)
        explosion[7] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion7)
        explosion[8] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion8)
        this.eX = initialX
        this.eY = initialY
    }

    fun getExplosion(explosionFrame: Int): Bitmap? {
        return explosion[explosionFrame]
    }
}
