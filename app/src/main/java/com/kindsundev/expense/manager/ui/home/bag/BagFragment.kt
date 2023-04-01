package com.kindsundev.expense.manager.ui.home.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.data.shared.PreferenceHelper
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
    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding

    private lateinit var bagPresenter: BagPresenter
    private lateinit var mWallets: ArrayList<WalletModel>
    private lateinit var mBills: ArrayList<BillModel>
    private lateinit var mCurrentWallet: WalletModel

    private var stateBalanceVisibility by Delegates.notNull<Boolean>()
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
        getStateBalanceVisibility()
        initWalletInfo()
        initRecyclerViewTransactions()
        initListener()
        return binding!!.root
    }
    private fun getStateBalanceVisibility() {
        stateBalanceVisibility = PreferenceHelper.getBoolean(
            requireContext(), Constant.BAG_FRAGMENT_BALANCE_VISIBILITY, true)
        checkDisplayBalance(stateBalanceVisibility)
    }

    private fun getDataFromHomeActivity() {
        mWallets = (activity as HomeActivity).getWallets()
        mBills = (activity as HomeActivity).getBills()
        mCurrentWallet = getCurrentWallet()
    }

    private fun getCurrentWallet(): WalletModel {
        val currentWalletId = (activity as HomeActivity).getCurrentWalletId()
        var walletModel: WalletModel? = null
        for (index in 0 until mWallets.size) {
            if (mWallets[index].id == currentWalletId) {
                walletModel = mWallets[index]
                break
            }
        }
        return walletModel!!
    }

    private fun initWalletInfo() {
        binding!!.tvName.text = mCurrentWallet.name.toString()
        binding!!.tvCurrency.text = mCurrentWallet.currency.toString()
        binding!!.tvBalance.text = amountFormatDisplay(mCurrentWallet.balance.toString())
    }

    private fun initRecyclerViewTransactions() {
        val mAdapter = BillParentAdapter(mCurrentWallet, mBills, this)
        binding!!.rcvTransactionsContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun initListener() {
        binding!!.btnUpgradePremium.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.btnVisibility.setOnClickListener { onClickVisibilityBalance() }
        binding!!.btnSearch.setOnClickListener { }
        binding!!.btnNotifications.setOnClickListener { }
    }

    private fun onClickVisibilityBalance() {
        stateBalanceVisibility = !stateBalanceVisibility
        checkDisplayBalance(stateBalanceVisibility)
    }

    private fun checkDisplayBalance(status: Boolean) {
        if (status) {
            binding!!.btnVisibility.setImageResource(R.drawable.ic_visibility)
            binding!!.tvCurrency.visibility = View.GONE
            binding!!.tvBalance.visibility = View.GONE
        } else {
            binding!!.btnVisibility.setImageResource(R.drawable.ic_visibility_off)
            binding!!.tvCurrency.visibility = View.VISIBLE
            binding!!.tvBalance.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        saveStateBalanceVisibility()
    }

    private fun saveStateBalanceVisibility() {
        PreferenceHelper.setBoolean(
            requireContext(), Constant.BAG_FRAGMENT_BALANCE_VISIBILITY, stateBalanceVisibility)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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