package com.kindsundev.expense.manager.ui.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.shared.PreferenceHelper
import com.kindsundev.expense.manager.databinding.FragmentBagBinding
import com.kindsundev.expense.manager.ui.home.wallet.exchange.ExchangeAdapter
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.showToast

class BagFragment : Fragment(), BagContract.Listener {
    private var _binding : FragmentBagBinding? = null
    private val binding get() = _binding

    private var stateBalanceVisibility: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        initRecyclerViewTransactions()
        initListener()
        return binding!!.root
    }

    private fun initRecyclerViewTransactions() {
        val mList = initTempList()
        val mAdapter = ExchangeAdapter(mList, this)
        binding!!.rcvTransactionsContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun initTempList(): ArrayList<String> {
        val mList = ArrayList<String>()
        for (i in 0 until 5) {
            mList.add("Transaction $i")
        }
        return mList
    }

    private fun initListener() {
        binding!!.btnUpgradePremium.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.btnVisibility.setOnClickListener { onClickVisibilityBalance() }
        binding!!.btnSearch.setOnClickListener {  }
        binding!!.btnNotifications.setOnClickListener {  }
    }

    private fun onClickVisibilityBalance() {
        if (stateBalanceVisibility) {
            binding!!.btnVisibility.setImageResource(R.drawable.ic_visibility)
            binding!!.itemBalance.visibility = View.GONE
            stateBalanceVisibility = false
        } else {
            binding!!.btnVisibility.setImageResource(R.drawable.ic_visibility_off)
            binding!!.itemBalance.visibility = View.VISIBLE
            stateBalanceVisibility = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stateBalanceVisibility = false
    }

    override fun onClickTransaction(transaction: String) {
        activity?.showToast(transaction)
    }
}