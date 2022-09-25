package com.example.android.messageapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class splashActivty : AppCompatActivity() {
    private val splashScreenTime = 4000
    private lateinit var image:ImageView
    private lateinit var topAnim:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activty)

        image = findViewById(R.id.splashLogo)
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim)
        image.animation = topAnim

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@splashActivty,AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        },splashScreenTime.toLong()
        )
    }
}