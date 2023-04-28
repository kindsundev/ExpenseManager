package com.kindsundev.expense.manager.ui.splash

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.ActivitySplashingBinding
import com.kindsundev.expense.manager.utils.isNetWorkAvailable
import com.kindsundev.expense.manager.utils.startPrepareWalletActivity
import com.kindsundev.expense.manager.utils.startSignInActivity
import kotlinx.coroutines.*

class SplashingActivity : AppCompatActivity(), SplashingContact.View {
    private lateinit var binding: ActivitySplashingBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private val splashPresenter = SplashingPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayoutFullScreen()
        checkNetworkStatus()
        splashPresenter.checkLoggedIn()
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

    private fun checkNetworkStatus() {
        if (isNetWorkAvailable(this)) {
            Toast.makeText(this, R.string.network_connected, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.network_disconnected, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }

    override fun isLoggedIn() {
        activityScope.launch {
            delay(1000)
            startPrepareWalletActivity()
        }
    }

    override fun notLoggedIn() {
        activityScope.launch {
            delay(1000)
            startSignInActivity()
            finishAffinity()
        }
    }
}