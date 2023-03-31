package com.kindsundev.expense.manager.ui.home

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.ActivityHomeBinding
import java.io.Serializable

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var wallets = ArrayList<WalletModel>()
    private var transactions = ArrayList<BillModel>()
    private var currentWalletId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNav()
        }
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
            wallets = it.parcelableArrayList(Constant.KEY_WALLET)!!
            transactions = it.serializable(Constant.KEY_BILL)!!
            currentWalletId = it.getInt(Constant.KEY_CURRENT_WALLET)
        }
    }

    private inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

    private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    fun getCurrentWalletId() = currentWalletId

    fun getWallets() = wallets

    fun getTransactionsOfWallet() = transactions

    override fun onDestroy() {
        super.onDestroy()
        wallets.clear()
        transactions.clear()
    }
}