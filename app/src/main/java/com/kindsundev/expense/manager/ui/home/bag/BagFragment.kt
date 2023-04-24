package com.kindsundev.expense.manager.ui.home.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillAdapterContract
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillParentAdapter
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionBottomSheet
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionDetailContract
import com.kindsundev.expense.manager.ui.home.bag.wallet.BagWalletBottomSheet
import com.kindsundev.expense.manager.utils.*
import kotlin.properties.Delegates

class BagFragment : Fragment(), BillAdapterContract.Listener, BagContract.View {
    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }
    private var stateBalanceVisibility by Delegates.notNull<Boolean>()

    private lateinit var bagPresenter: BagPresenter
    private lateinit var walletBottomSheet: BagWalletBottomSheet
    private lateinit var mWallets: ArrayList<WalletModel>
    private lateinit var mCurrentWalletId: String
    private lateinit var mCurrentWallet: WalletModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        getCurrentWalletId()
        bagPresenter = BagPresenter(this)
        bagPresenter.handleGetWallets()
        getStateBalanceVisibility()
        initListener()
        return binding!!.root
    }

    /*
    * This function is used to get the wallet id stored in preferences.
    * - Purpose is to restore previously selected user wallet when they return to this fragment
    * - Flow: save in stop lifecycle and retrieve in onCreate then init it
    * Note: Set it == null in destroy lifecycle, then everything works fine
    * */
    private fun getCurrentWalletId() {
        mCurrentWalletId = (context as HomeActivity).getCurrentWalletId()
        if (mCurrentWalletId == Constant.VALUE_DATA_IS_NULL) {
            mCurrentWalletId = PreferenceHelper.getString(
                requireContext(),
                Constant.BAG_FRAGMENT_BEFORE_WALLET_ID,
                Constant.VALUE_DATA_IS_NULL
            ).toString()
        }
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

    private fun initListener() {
        binding!!.btnUpgradePremium.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.btnVisibility.setOnClickListener { onClickVisibilityBalance() }
        binding!!.rlWallet.setOnClickListener { onClickSelectWallet() }
        binding!!.btnSearch.setOnClickListener { onClickSearchBalance() }
        binding!!.btnNotifications.setOnClickListener { }
    }

    private fun onClickVisibilityBalance() {
        stateBalanceVisibility = !stateBalanceVisibility
        checkDisplayBalance(stateBalanceVisibility)
    }

    private fun onClickSelectWallet() {
        walletBottomSheet = BagWalletBottomSheet(mWallets, object : BagContract.Listener {
            override fun onClickWalletItem(wallet: WalletModel) {
                mCurrentWalletId = wallet.id.toString()
                getTransactionsOfCurrentWallet()
            }
        })
        walletBottomSheet.show(parentFragmentManager, Constant.BUDGET_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    private fun getTransactionsOfCurrentWallet() {
        mCurrentWallet = getCurrentWallet(mCurrentWalletId)
        bagPresenter.handleGetBillsAndSort(mCurrentWallet)
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

    private fun onClickSearchBalance() {
        val action = BagFragmentDirections.actionBagFragmentToTransactionSearchFragment(mCurrentWallet)
        findNavController().navigate(action)
    }

    override fun onClickTransaction(date: String, transaction: TransactionModel) {
        val bottomSheet = TransactionBottomSheet(object : TransactionDetailContract.Result {
            override fun notificationSuccess(result: Boolean) {
                if (result) { bagPresenter.handleGetWallets() }
            }
        }, mCurrentWallet, date, transaction)
        bottomSheet.show(parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_TRANSACTION_NAME)
    }

    override fun onSuccess() {}

    override fun onSuccessWallets(result: ArrayList<WalletModel>) {
        mWallets = result
        getTransactionsOfCurrentWallet()
    }

    override fun onSuccessBills(result: ArrayList<BillModel>) {
        initCurrentWalletInfo()
        initRecyclerViewTransactions(result)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initCurrentWalletInfo() {
        binding!!.tvName.text = mCurrentWallet.name.toString()
        binding!!.tvCurrency.text = mCurrentWallet.currency.toString()
        binding!!.tvBalance.text = formatDisplayCurrencyBalance(mCurrentWallet.balance.toString())
    }

    private fun initRecyclerViewTransactions(data: ArrayList<BillModel>) {
        val mAdapter = BillParentAdapter(data, this)
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

    override fun onStop() {
        super.onStop()
        PreferenceHelper.setBoolean(
            requireContext(), Constant.BAG_FRAGMENT_BALANCE_VISIBILITY, stateBalanceVisibility
        )
        PreferenceHelper.setString(
            requireContext(), Constant.BAG_FRAGMENT_BEFORE_WALLET_ID, mCurrentWalletId
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (context as HomeActivity).setCurrentWalletIdDefaultValue()
        bagPresenter.cleanUp()
    }
}