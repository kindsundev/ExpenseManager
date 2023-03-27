package com.kindsundev.expense.manager.ui.home

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.ActivityHomeBinding
import kotlin.properties.Delegates

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var wallets = ArrayList<WalletModel>()
    private var transactions = ArrayList<TransactionModel>()
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                wallets = it.getParcelableArrayList(Constant.KEY_WALLET, WalletModel::class.java)
                            as ArrayList<WalletModel>
                transactions = it.getParcelableArrayList(Constant.KEY_TRANSACTION, TransactionModel::class.java)
                            as ArrayList<TransactionModel>
            } else {
                wallets = it.getParcelableArrayList<WalletModel>(Constant.KEY_WALLET)
                        as ArrayList<WalletModel>
                transactions = it.getParcelableArrayList<TransactionModel>(Constant.KEY_TRANSACTION)
                        as ArrayList<TransactionModel>
            }
            currentWalletId = it.getInt(Constant.KEY_CURRENT_WALLET)
        }
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