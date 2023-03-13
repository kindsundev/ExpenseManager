package com.kindsundev.expense.manager.ui.home.note.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentTransactionBinding
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletListener

class TransactionFragment : Fragment(), WalletListener {
    private var _binding : FragmentTransactionBinding? = null
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
        binding!!.tvCategoryName.text = categoryName
        initWalletBottomSheet()
        initListener()
        return binding!!.root
    }

    private fun initWalletBottomSheet() {
        // After config realtime database, get data then bind here
        val tempData = listOf("Wallet 1", "Wallet 2", "Wallet 3", "Wallet 4")
        walletBottomSheet = WalletBottomSheet(tempData, this)
        walletBottomSheet.show(parentFragmentManager, Constant.WALLET_BOTTOM_SHEET_NAME)
    }

    override fun onClickWalletItem(data: String) {
        Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
        binding!!.tvTransactionName.text = data
    }

    private fun initListener() {
        binding!!.tvTransactionName.setOnClickListener { initWalletBottomSheet() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}