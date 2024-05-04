package com.example.witchhunterkt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Handler
import android.view.Display
import android.view.MotionEvent
import android.view.View
import java.util.*

class WitchHunter(context: Context) : View(context) {

    private var background: Bitmap
    private var heartImage: Bitmap
    private val handler: Handler = Handler()
    private val UPDATE_MILLIS: Long = 30
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var points = 0
    private var life = 3
    private var highScore = 0
    private val scorePaint: Paint = Paint()
    private val TEXT_SIZE = 80f
    private val ourWizard: OurWizard
    private val enemyWitch: EnemyWitch
    private val wizardShoots: ArrayList<ShootWizard> = ArrayList()
    private val witchShoots: ArrayList<ShootWitch> = ArrayList()
    private val explosions: ArrayList<Explotion> = ArrayList()
    private var enemyShootAction = false
    private var playerInvincible = false
    private var lastUpdateTime = 0L
    private var gameEnded = false
    private lateinit var mediaPlayer: MediaPlayer



    init {
        val display: Display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
        ourWizard = OurWizard(context, screenWidth, screenHeight)
        enemyWitch = EnemyWitch(context)
        background = BitmapFactory.decodeResource(context.resources, R.drawable.bc1)
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true)
        heartImage = BitmapFactory.decodeResource(context.resources, R.drawable.heart)
        val heartSize = screenWidth / 20
        heartImage = Bitmap.createScaledBitmap(heartImage, heartSize, heartSize, true)
        scorePaint.color = Color.RED
        scorePaint.textSize = TEXT_SIZE
        mediaPlayer = MediaPlayer.create(context as Activity, R.raw.sinister)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        loadHighScore()
        startGame()

    }

    private fun startGame() {
        resetGame()
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateGame()
                invalidate()
                handler.postDelayed(this, UPDATE_MILLIS)
            }
        }, UPDATE_MILLIS)
    }


    private fun resetGame() {
        points = 0
        life = 3
        wizardShoots.clear()
        witchShoots.clear()
        explosions.clear()
        gameEnded = false
    }

    private fun updateGame() {
        if (!gameEnded) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastUpdateTime

            if (elapsedTime >= UPDATE_MILLIS) {
                lastUpdateTime = currentTime

                if (!playerInvincible) {
                    // Update enemy movement
                    enemyWitch.ex += enemyWitch.enemyVelocity
                    if (enemyWitch.ex + enemyWitch.getEnemyWitchWidth() >= screenWidth || enemyWitch.ex <= 0) {
                        enemyWitch.enemyVelocity *= -1
                    }

                    // Trigger enemy shooting
                    if (!enemyShootAction) {
                        val shootDelay = 1000 // Delay between each witch shoot in milliseconds
                        handler.postDelayed({
                            val witchShoot = ShootWitch(context, enemyWitch.ex + enemyWitch.getEnemyWitchWidth() / 2, enemyWitch.ey)
                            witchShoots.add(witchShoot)
                            enemyShootAction = false
                        }, shootDelay.toLong())
                        enemyShootAction = true
                    }

                    // Update witch shoots and check for collisions with player
                    val witchesToRemove: ArrayList<ShootWitch> = ArrayList()
                    for (witchShoot in witchShoots) {
                        witchShoot.shy += 35 // Adjust the witch shoot speed as needed
                        if (witchShoot.shy >= screenHeight) {
                            witchesToRemove.add(witchShoot)
                        } else if (witchShoot.shx + witchShoot.getShotWidth() >= ourWizard.ox &&
                            witchShoot.shx <= ourWizard.ox + ourWizard.getOurWizardWidth() &&
                            witchShoot.shy + witchShoot.getShotHeight() >= ourWizard.oy &&
                            witchShoot.shy <= ourWizard.oy + ourWizard.getOurWizardHeight()) {
                            witchesToRemove.add(witchShoot)
                            handlePlayerHit()
                        }
                    }
                    witchShoots.removeAll(witchesToRemove)
                }

                // Update wizard shoots and check for collisions with enemy witch
                updateWizardShoots()

                // Check game end conditions
                checkGameEndConditions()
            }
        }
    }


    private fun handlePlayerHit() {
        if (!playerInvincible) {
            life--
            if (life <= 0) {
                endGame()
            } else {
                playerInvincible = true
                val explosion = Explotion(context, ourWizard.ox, ourWizard.oy)
                explosions.add(explosion)
                playerInvincible = false
            }
        }
    }

    private fun updateWizardShoots() {
        val shootsToRemove: ArrayList<ShootWizard> = ArrayList()
        for (shoot in wizardShoots) {
            shoot.shy -= 25 // Adjust the shoot speed as needed
            if (shoot.shy <= 0) {
                shootsToRemove.add(shoot)
                continue
            }
            // Check for collision with enemy witch
            if (shoot.shx + shoot.getShotWidth() >= enemyWitch.ex &&
                shoot.shx <= enemyWitch.ex + enemyWitch.getEnemyWitchWidth() &&
                shoot.shy + shoot.getShotHeight() >= enemyWitch.ey &&
                shoot.shy <= enemyWitch.ey + enemyWitch.getEnemyWitchHeight() / 2) {
                shootsToRemove.add(shoot)
                val explosion = Explotion(context, enemyWitch.ex, enemyWitch.ey)
                explosions.add(explosion)
                points++
                break
            }
        }
        wizardShoots.removeAll(shootsToRemove)


        if (wizardShoots.isEmpty()) {
            val ourShot = ShootWizard(context, ourWizard.ox + ourWizard.getOurWizardWidth() / 2, ourWizard.oy)
            wizardShoots.add(ourShot)
        }
    }

    private fun checkGameEndConditions() {
        if (life <= 0 && !gameEnded) {
            endGame()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background
        canvas.drawBitmap(background, 0f, 0f, null)

        // Draw explosions
        val explosionsToRemove: ArrayList<Explotion> = ArrayList()
        for (explosion in explosions) {
            val explosionBitmap = explosion.getExplosion(explosion.explosionFrame) ?: continue
            canvas.drawBitmap(explosionBitmap, explosion.eX.toFloat(), explosion.eY.toFloat(), null)
            explosion.explosionFrame++
            if (explosion.explosionFrame > 8) {
                explosionsToRemove.add(explosion)
            }
        }
        explosions.removeAll(explosionsToRemove)

        // Draw score
        canvas.drawText("Score: $points", 0f, TEXT_SIZE, scorePaint)

        // Draw hearts
        for (i in life downTo 1) {
            canvas.drawBitmap(heartImage, (screenWidth - heartImage.width * i).toFloat(), 0f, null)
        }

        // Draw enemy witch
        canvas.drawBitmap(enemyWitch.getEnemyWitchBitmap(), enemyWitch.ex.toFloat(), enemyWitch.ey.toFloat(), null)

        // Draw our wizard
        canvas.drawBitmap(ourWizard.ourWizard, ourWizard.ox.toFloat(), ourWizard.oy.toFloat(), null)

        // Draw wizard shoots
        for (shoot in wizardShoots) {
            canvas.drawBitmap(shoot.wizardShoot, shoot.shx.toFloat(), shoot.shy.toFloat(), null)
        }

        // Draw witch shoots
        for (shoot in witchShoots) {
            canvas.drawBitmap(shoot.witchShoot, shoot.shx.toFloat(), shoot.shy.toFloat(), null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()

        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            ourWizard.ox = touchX
        }

        if (event.action == MotionEvent.ACTION_UP) {
            val ourShot = ShootWizard(context, ourWizard.ox + ourWizard.getOurWizardWidth() / 2, ourWizard.oy)
            wizardShoots.add(ourShot)
            val witchShot = ShootWitch(context, enemyWitch.ex + enemyWitch.getEnemyWitchWidth() / 2, enemyWitch.ey)
            witchShoots.add(witchShot)
        }

        return true
    }

    private fun endGame() {

        mediaPlayer.release()
        gameEnded = true
        if (points > highScore) {
            highScore = points
            saveHighScore()
        }
        handler.removeCallbacksAndMessages(null) // Remove all callbacks and messages
        val intent = Intent(context, GameOver::class.java)
        intent.putExtra("points", points)
        intent.putExtra("highScore", highScore)
        context.startActivity(intent)
        (context as Activity).finish()

    }

    private fun saveHighScore() {
        val prefs: SharedPreferences = context.getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt("highScore", highScore)
        editor.apply()
    }

    private fun loadHighScore() {
        val prefs: SharedPreferences = context.getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        highScore = prefs.getInt("highScore", 0)
    }

}
