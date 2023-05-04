package com.kindsundev.expense.manager.ui.home.budget.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.budget.wallet.detail.BudgetWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.budget.wallet.detail.BudgetWalletDetailContract
import com.kindsundev.expense.manager.ui.home.budget.wallet.dialog.CreateWalletDialog
import com.kindsundev.expense.manager.utils.toggleBottomNavigation
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class BudgetWalletFragment : Fragment(),
    BudgetWalletContract.View, BudgetWalletContract.Listener{
    private var _binding: FragmentBudgetWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var walletPresenter: BudgetWalletPresenter
    private lateinit var mWalletAdapter : BudgetWalletAdapter
    private lateinit var mWalletDetailBottomSheet: BudgetWalletBottomSheet

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletPresenter = BudgetWalletPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetWalletBinding.inflate(layoutInflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        walletPresenter.handleGetWallets()
        initListener()
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
        _binding = null
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
    }
}