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
import com.kindsundev.expense.manager.ui.custom.DateSelectionDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillParentAdapter
import com.kindsundev.expense.manager.ui.home.bag.detail.ResultTransactionCallback
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionBottomSheet
import com.kindsundev.expense.manager.utils.*
import kotlin.properties.Delegates

class BagFragment : Fragment(), BagContract.Listener, BagContract.ViewParent {
    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding
    private var stateBalanceVisibility by Delegates.notNull<Boolean>()
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var bagPresenter: BagPresenter
    private lateinit var mWallets: ArrayList<WalletModel>
    private lateinit var mBills: ArrayList<BillModel>
    private lateinit var mCurrentWallet: WalletModel

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
        bagPresenter.handlerGetWallets()
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnUpgradePremium.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.btnVisibility.setOnClickListener { onClickVisibilityBalance() }
        binding!!.btnSearch.setOnClickListener { onClickSearchBalance() }
        binding!!.btnNotifications.setOnClickListener { }
    }

    private fun onClickVisibilityBalance() {
        stateBalanceVisibility = !stateBalanceVisibility
        checkDisplayBalance(stateBalanceVisibility)
    }

    private fun onClickSearchBalance() {
        DateSelectionDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                activity?.showToast(newDateTime)
            }
        }).onShowDatePickerDialog()
    }

    override fun onStop() {
        super.onStop()
        PreferenceHelper.setBoolean(
            requireContext(), Constant.BAG_FRAGMENT_BALANCE_VISIBILITY, stateBalanceVisibility
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bagPresenter.cleanUp()
    }

    override fun onClickTransaction(transaction: TransactionModel) {
        val bottomSheet = TransactionBottomSheet(object : ResultTransactionCallback {
            override fun transactionUpdated(result: Boolean) {
                if (result) {  // refresh data
                    bagPresenter.handlerGetWallets()
                }
            }
        }, mCurrentWallet, transaction)
        bottomSheet.show(parentFragmentManager, Constant.WALLET_BOTTOM_SHEET_TRANSACTION_NAME)
    }

    override fun onSuccess() {
        mWallets = bagPresenter.getWallets()
        getTransactionsOfCurrentWallet()
    }

    private fun getTransactionsOfCurrentWallet() {
        val mCurrentWalletId = (context as HomeActivity).getCurrentWalletId()
        mCurrentWallet = getCurrentWallet(mCurrentWalletId)
        bagPresenter.handlerGetTransactions(mCurrentWalletId)
    }

    private fun getCurrentWallet(id: String): WalletModel {
        var wallet = WalletModel()
        for (index in 0 until mWallets.size) {
            if (mWallets[index].id == id.toInt()) {
                wallet = mWallets[index]
                break
            }
        }
        return wallet
    }

    override fun onSuccess(status: Boolean) {
        mBills = bagPresenter.getBills()
        initDataToUi()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    override fun onSuccess(message: String) {}

    private fun initDataToUi() {
        initCurrentWalletInfo()
        initRecyclerViewTransactions()
    }

    private fun initCurrentWalletInfo() {
        getStateBalanceVisibility()
        binding!!.tvName.text = mCurrentWallet.name.toString()
        binding!!.tvCurrency.text = mCurrentWallet.currency.toString()
        binding!!.tvBalance.text = amountFormatDisplay(mCurrentWallet.balance.toString())
    }

    private fun getStateBalanceVisibility() {
        stateBalanceVisibility = PreferenceHelper.getBoolean(
            requireContext(), Constant.BAG_FRAGMENT_BALANCE_VISIBILITY, true
        )
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

    private fun initRecyclerViewTransactions() {
        val mAdapter = BillParentAdapter(mBills, this)
        binding!!.rcvTransactionsContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        initCurrentWalletInfo()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }
}