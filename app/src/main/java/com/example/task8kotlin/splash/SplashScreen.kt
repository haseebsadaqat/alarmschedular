package com.example.task8kotlin.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.example.task7kotlin.utils.Constants
import com.example.task8kotlin.R
import com.example.task8kotlin.databinding.ActivitySplashScreenBinding
import com.example.task8kotlin.main.MainActivity

class SplashScreen : AppCompatActivity() {
    lateinit var im: ImageView
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding . inflate (layoutInflater)
        setContentView(binding.root)
        //initializing Variables
        initialSetup()
        //splash screen 3 seconds
        val updateHandler = Handler()
        val runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        updateHandler.postDelayed(runnable, Constants.splashTime.toLong())
    }
    private fun initialSetup() {
        im=binding.splashimage
        im.setImageResource(R.drawable.splashalarmm)
        im.scaleType=ImageView.ScaleType.FIT_CENTER
    }
}