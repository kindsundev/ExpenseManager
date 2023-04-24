package com.kindsundev.expense.manager.ui.home.note.transaction.debt

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.databinding.BottomSheetSelectDebtAuthorBinding
import com.kindsundev.expense.manager.ui.home.note.transaction.TransactionFragment

class TransactionDebtBottomSheet(
    private val listener: TransactionDebtContract.Listener
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSelectDebtAuthorBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectDebtAuthorBinding.inflate(inflater)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.debtBottomSheetHasDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}