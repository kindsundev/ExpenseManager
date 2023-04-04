package com.kindsundev.expense.manager.ui.home.bag.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetTransactionDetailBinding
import com.kindsundev.expense.manager.ui.custom.CallbackDateTime
import com.kindsundev.expense.manager.ui.custom.DateTimePicker
import com.kindsundev.expense.manager.utils.amountFormatDisplay
import com.kindsundev.expense.manager.utils.toEditable

class TransactionBottomSheet(
    private val wallet: WalletModel,
    private val transaction: TransactionModel
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetTransactionDetailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTransactionDetailBinding.inflate(layoutInflater)
        initTransactionData()
        initListener()
        return binding!!.root
    }

    private fun initTransactionData() {
        val amount = amountFormatDisplay(transaction.amount.toString()).trim()
        binding!!.edtAmount.text = amount.toEditable()
        binding!!.tvWalletName.text = wallet.name
        binding!!.tvCategoryName.text = transaction.category
        binding!!.tvTime.text = transaction.date
        if (transaction.note != "") {
            val note = transaction.note.toString()
            binding!!.edtDescription.text = note.toEditable()
        }
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { hideBottomSheet() }
        binding!!.btnSave.setOnClickListener {  }
        binding!!.btnRemove.setOnClickListener {  }
        binding!!.itemCategory.setOnClickListener { onClickSetCategoryName() }
        binding!!.itemTime.setOnClickListener { onClickSetDateTime() }
    }

    private fun hideBottomSheet() : Unit = this.dismiss()

    private fun onClickSetCategoryName() {

    }

    private fun onClickSetDateTime() {
        DateTimePicker(requireContext(), object : CallbackDateTime {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvTime.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}