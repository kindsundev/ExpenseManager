package com.kindsundev.expense.manager.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        checkNetworkStatus()
        waitAndNextActivity()
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