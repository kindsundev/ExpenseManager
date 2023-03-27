package com.kindsundev.expense.manager.ui.prepare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.kindsundev.expense.manager.databinding.ActivityPrepareWalletBinding
import com.kindsundev.expense.manager.ui.prepare.pager.WalletViewPager
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.startHomeActivity

class PrepareWalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrepareWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrepareWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWalletViewPager()
    }

    private fun initWalletViewPager() {
        binding.viewPagerWallet.adapter = WalletViewPager(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabPrepareWallet, binding.viewPagerWallet) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Prepare"
                }
                else -> {
                    tab.text = "Provide"
                }
            }
        }.attach()
    }
}