package com.kindsundev.expense.manager.ui.home.budget.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.budget.wallet.adapter.BudgetWalletAdapter
import com.kindsundev.expense.manager.utils.displaySwitchBottomNavigation
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class BudgetWalletFragment : Fragment(), BudgetWalletContract.View, BudgetWalletContract.Listener {
    private var _binding: FragmentBudgetWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }
    private val args: BudgetWalletFragmentArgs by navArgs()

    private lateinit var budgetWalletPresenter: BudgetWalletPresenter
    private lateinit var mWallet: ArrayList<WalletModel>
    private lateinit var mWalletAdapter : BudgetWalletAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, false)
        budgetWalletPresenter = BudgetWalletPresenter(this)
        mWallet = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetWalletBinding.inflate(layoutInflater)
        budgetWalletPresenter.handlerGetWallets()
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding!!.btnAdd.setOnClickListener { onClickCreateWallet() }
    }

    private fun onClickCreateWallet() {
        activity?.showToast("onClickCreateWallet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess() {
        initRecyclerViewWallets()
        checkAndActionAsRequired()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initRecyclerViewWallets() {
        mWallet = budgetWalletPresenter.getWallets()
        mWalletAdapter = BudgetWalletAdapter(mWallet, this)
        binding!!.rcvWallets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mWalletAdapter
        }
    }

    private fun checkAndActionAsRequired() {
        when (args.keyAction) {
            Constant.ACTION_CREATE_WALLET -> {
                // call dialog
            }
            Constant.ACTION_UPDATE_WALLET -> {
                val message = "Please select the wallet you want to update!"
                showNotificationRequired(message)
            }
            else -> {
                val message = "Please select the wallet you want to delete!"
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
        activity?.showToast("onClickEditWallet")
    }

    override fun onClickDeleteWallet(walletId: String) {
        activity?.showToast("onClickDeleteWallet")
    }
}