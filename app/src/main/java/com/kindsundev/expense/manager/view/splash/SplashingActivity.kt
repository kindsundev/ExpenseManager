package com.kindsundev.expense.manager.view.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.ActivitySplashingBinding
import com.kindsundev.expense.manager.utils.isNetWorkAvailable
import com.kindsundev.expense.manager.view.signin.SignInActivity
import kotlinx.coroutines.*

class SplashingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashingBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayoutFullScreen()
        checkNetworkStatus()
        waitAndNextActivity()
    }

    private fun setLayoutFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun waitAndNextActivity() {
        activityScope.launch {
            delay(1000)
            startActivity(Intent(this@SplashingActivity, SignInActivity::class.java))
            finishAffinity()
        }
    }

    private fun checkNetworkStatus() {
        if (isNetWorkAvailable(this)) {
            Toast.makeText(this, "Network connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Network disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}