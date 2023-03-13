package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.databinding.BottomSheetWalletBinding

class WalletBottomSheet(
    private val mListItems: List<String>,
    private val mListener: WalletListener
) : BottomSheetDialogFragment() {
    private var _binding : BottomSheetWalletBinding? = null
    private val binding get() = _binding

    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWalletBinding.inflate(layoutInflater)
        initRecyclerView()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.layoutManager = LinearLayoutManager(context)
        walletAdapter = WalletAdapter(mListItems, mListener)
        binding!!.rvWallets.adapter = walletAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}