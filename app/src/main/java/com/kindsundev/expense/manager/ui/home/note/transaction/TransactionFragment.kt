package com.kindsundev.expense.manager.ui.home.note.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentTransactionBinding
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletContract
import com.kindsundev.expense.manager.utils.getCurrentTime
import com.kindsundev.expense.manager.utils.requestPremium
import com.kindsundev.expense.manager.utils.showToast

class TransactionFragment : Fragment(),
    WalletContract.Listener, TransactionContract.View {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding
    private var categoryName: String? = null

    private lateinit var walletBottomSheet: WalletBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryName = arguments?.getString(Constant.CATEGORY_TRANSACTION_NAME).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater)
        initView()
        initWalletBottomSheet()
        initListener()
        return binding!!.root
    }

    private fun initView() {
        binding!!.tvCategoryName.text = categoryName
        binding!!.tvTime.text = getCurrentTime()
    }

    private fun initWalletBottomSheet() {
        walletBottomSheet = WalletBottomSheet(this)
        walletBottomSheet.show(parentFragmentManager, Constant.WALLET_BOTTOM_SHEET_NAME)
    }

    override fun onClickWalletItem(wallet: WalletModel) {
        binding!!.tvTransactionName.text = wallet.name
        walletBottomSheet.hideBottomSheet()
    }

    private fun initListener() {
        binding!!.tvTransactionName.setOnClickListener { initWalletBottomSheet() }
        binding!!.itemCategory.setOnClickListener { findNavController().popBackStack() }
        binding!!.itemEvent.setOnClickListener { activity?.requestPremium() }
        binding!!.switchOptimization.setOnClickListener { activity?.requestPremium() }
        binding!!.tvShowMore.setOnClickListener { activity?.requestPremium() }

        binding!!.btnSave.setOnClickListener {
            activity?.showToast("Create transaction success")
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoad() {
        TODO("Not yet implemented")
    }

    override fun onError(message: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }
}