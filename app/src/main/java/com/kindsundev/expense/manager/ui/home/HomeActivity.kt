package com.kindsundev.expense.manager.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var currentWalletId: String = Constant.VALUE_DATA_IS_NULL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        getDataFromPrepareWallet()
    }

    private fun setupBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment)
        navHostFragment?.let {
            val navController = navHostFragment.findNavController()
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }

    private fun getDataFromPrepareWallet() {
        val bundle = intent.extras
        bundle?.let {
            currentWalletId = it.getString(Constant.KEY_CURRENT_WALLET_ID, Constant.VALUE_DATA_IS_NULL)
        }
    }

    fun getCurrentWalletId() = currentWalletId

    fun setCurrentWalletIdDefaultValue() { currentWalletId = Constant.VALUE_DATA_IS_NULL }
}