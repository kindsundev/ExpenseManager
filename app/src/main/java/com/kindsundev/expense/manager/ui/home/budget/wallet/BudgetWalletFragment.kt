package com.kindsundev.expense.manager.ui.home.budget.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.budget.wallet.adapter.BudgetWalletAdapter
import com.kindsundev.expense.manager.ui.home.budget.wallet.detail.BudgetWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.budget.wallet.detail.BudgetWalletDetailContract
import com.kindsundev.expense.manager.utils.toggleBottomNavigation
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class BudgetWalletFragment : Fragment(),
    BudgetWalletContract.View, BudgetWalletContract.Listener{
    private var _binding: FragmentBudgetWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }
    private val args: BudgetWalletFragmentArgs by navArgs()

    private lateinit var walletPresenter: BudgetWalletPresenter
    private lateinit var mWalletAdapter : BudgetWalletAdapter
    private lateinit var mWalletDetailBottomSheet: BudgetWalletBottomSheet

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        walletPresenter = BudgetWalletPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetWalletBinding.inflate(layoutInflater)
        walletPresenter.handleGetWallets()
        initListener()
        checkAndActionAsRequired()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding!!.btnAdd.setOnClickListener { onClickCreateWallet() }
    }

    private fun onClickCreateWallet() {
        val createWalletDialog = CreateWalletDialog(object : BudgetWalletContract.Result {
            override fun onResultCreateWallet(status: Boolean) {
                if (status) { walletPresenter.handleGetWallets() }
            }
        })
        createWalletDialog.show(parentFragmentManager, createWalletDialog.tag)
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {}

    override fun onSuccessWallets(wallets: ArrayList<WalletModel>) {
        initRecyclerViewWallets(wallets)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initRecyclerViewWallets(wallets: ArrayList<WalletModel>) {
        mWalletAdapter = BudgetWalletAdapter(wallets, this)
        binding!!.rcvWallets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mWalletAdapter
        }
    }

    private fun checkAndActionAsRequired() {
        val message: String
        when (args.keyAction) {
            Constant.ACTION_CREATE_WALLET -> {
                onClickCreateWallet()
            }
            Constant.ACTION_UPDATE_WALLET -> {
                message = getCurrentContext().getString(R.string.request_select_wallet_update)
                showNotificationRequired(message)
            }
            else -> {
                message = getCurrentContext().getString(R.string.request_select_wallet_delete)
                showNotificationRequired(message)
            }
        }
    }

    private fun showNotificationRequired(message: String) {
        val snackBar = Snackbar.make(
            binding!!.coordinatorBudgetWalletContainer,
            message, Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("OK") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    override fun onClickEditWallet(wallet: WalletModel) {
        mWalletDetailBottomSheet = BudgetWalletBottomSheet(wallet, object : BudgetWalletDetailContract.Result {
            override fun onSuccessAndRequiredRefreshData(status: Boolean) {
                if (status) { refreshData() }
            }
        })
        mWalletDetailBottomSheet.show(parentFragmentManager, Constant.BUDGET_WALLET_BOTTOM_SHEET_DETAIL_WALLET_NAME)
    }

    private fun refreshData() {
        walletPresenter.handleGetWallets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}