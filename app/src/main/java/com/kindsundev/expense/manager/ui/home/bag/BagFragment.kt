package com.kindsundev.expense.manager.ui.home.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBagBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.bag.exchange.BillParentAdapter
import com.kindsundev.expense.manager.utils.amountFormatDisplay
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog
import kotlin.properties.Delegates

class BagFragment : Fragment(), BagContract.Listener, BagContract.View {
    private var _binding : FragmentBagBinding? = null
    private val binding get() = _binding

    private lateinit var bagPresenter: BagPresenter
    private var currentWalletId by Delegates.notNull<Int>()
    private lateinit var mWallets: ArrayList<WalletModel>
    private lateinit var mBills: ArrayList<BillModel>

    private var stateBalanceVisibility: Boolean = true
    private val loadingDialog by lazy { LoadingDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bagPresenter = BagPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        getDataFromHomeActivity()
        initWalletInfo()
        initRecyclerViewTransactions()
        initListener()
        return binding!!.root
    }

    private fun getDataFromHomeActivity() {
        currentWalletId = (activity as HomeActivity).getCurrentWalletId()
        mWallets = (activity as HomeActivity).getWallets()
        mBills = (activity as HomeActivity).getBills()
    }

    private fun initWalletInfo() {
        for (index in 0 until mWallets.size) {
            if (mWallets[index].id == currentWalletId) {
                val currentWallet = mWallets[index]
                binding!!.tvName.text = currentWallet.name.toString()
                binding!!.tvCurrency.text = currentWallet.currency.toString()
                binding!!.tvBalance.text = amountFormatDisplay(currentWallet.balance.toString())
                break
            }
        }
    }

    private fun initRecyclerViewTransactions() {
        val mAdapter = BillParentAdapter(mBills, this)
        binding!!.rcvTransactionsContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
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
            binding!!.tvCurrency.visibility = View.GONE
            binding!!.tvBalance.visibility = View.GONE
            stateBalanceVisibility = false
        } else {
            binding!!.btnVisibility.setImageResource(R.drawable.ic_visibility_off)
            binding!!.tvCurrency.visibility = View.VISIBLE
            binding!!.tvBalance.visibility = View.VISIBLE
            stateBalanceVisibility = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stateBalanceVisibility = false
    }

    override fun onClickTransaction(transaction: TransactionModel) {
        activity?.showToast(transaction.type.toString())
    }

    override fun onSuccess(key: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    override fun onSuccess() {}

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }
}