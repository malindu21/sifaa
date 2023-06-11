package com.pro.cafy_theofficecafeteria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SifaaSplashActivity : AppCompatActivity() {

    private lateinit var appLogoBack: ImageView
    private lateinit var appTextBack: ImageView

    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.flash_red_color)
        window.navigationBarColor = resources.getColor(R.color.flash_red_color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_splash_activity)

        appLogoBack = findViewById(R.id.splash_screen_app_logo_back_iv)
        appTextBack = findViewById(R.id.splash_screen_app_logo_iv)

        appLogoBack.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_decrease_anim))
        appTextBack.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_increase_anim))

        Handler().postDelayed({

            val intent = Intent(this, SIfaaUserLoginActivity::class.java)
            startActivity(intent)
            finish()

        }, 1200)

    }
}