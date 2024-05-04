package com.example.witchhunterkt


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Assuming the WitchHunter view is added programmatically
        val witchHunterView = WitchHunter(this)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addContentView(witchHunterView, layoutParams)
    }
}