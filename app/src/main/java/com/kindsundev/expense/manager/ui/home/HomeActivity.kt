package com.kindsundev.expense.manager.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.databinding.ActivityHomeBinding
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        val user = UserFirebase().user
        Logger.warn(user?.displayName.toString())
        Logger.warn(user?.email.toString())
    }

    private fun setupBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment)
        navHostFragment?.let {
            val navController = navHostFragment.findNavController()
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }
}