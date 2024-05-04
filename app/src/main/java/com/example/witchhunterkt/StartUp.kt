package com.example.witchhunterkt

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartUp : AppCompatActivity() {

    private lateinit var tvHighScore: TextView
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)


        tvHighScore = findViewById(R.id.tvHighScore)


        val highScore = getHighScore()


        tvHighScore.text = getString(R.string.high_score, highScore)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.isLooping = true // sound is looping
        mediaPlayer.start()
    }

    private fun getHighScore(): Int {
        val prefs: SharedPreferences = getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        return prefs.getInt("highScore", 0) // Default value is 0
    }

    fun startGame(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
