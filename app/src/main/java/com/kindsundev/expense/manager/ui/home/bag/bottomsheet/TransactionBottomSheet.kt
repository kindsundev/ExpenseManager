package com.kindsundev.expense.manager.ui.home.bag.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetTransactionDetailBinding
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.custom.DateTimePicker
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.bag.BagContract
import com.kindsundev.expense.manager.ui.home.bag.BagPresenter
import com.kindsundev.expense.manager.utils.*

class TransactionBottomSheet(
    private val callback: ResultTransactionCallback,
    private val wallet: WalletModel,
    private val transaction: TransactionModel
) : BottomSheetDialogFragment(), BagContract.ViewParent {
    private var _binding: BottomSheetTransactionDetailBinding? = null
    private val binding get() = _binding

    private lateinit var bagPresenter: BagPresenter
    private val loadingDialog by lazy { LoadingDialog() }
    private lateinit var mNewTransaction: TransactionModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bagPresenter = BagPresenter(bagView = this, adapterView = null)
    }

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
        binding!!.itemTime.setOnClickListener { onClickSetDateTime() }
        binding!!.btnSave.setOnClickListener { onClickUpdateTransaction() }
        binding!!.btnRemove.setOnClickListener {  }
    }

    private fun hideBottomSheet() : Unit = this.dismiss()

    private fun onClickSetDateTime() {
        DateTimePicker(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvTime.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickUpdateTransaction() {
        if (binding!!.edtAmount.text.isEmpty()) {
            activity?.showToast("Please enter amount value!")
        } else {
            val oldValue = transaction.amount
            val newValue = binding!!.edtAmount.text.toString().toDouble()
            if (oldValue == newValue) {
                activity?.showToast("Please do not enter the old money")
            } else {
                mNewTransaction = getNewTransaction()
                bagPresenter.updateTransaction(wallet.id!!, mNewTransaction)
            }
        }
    }

    private fun getNewTransaction(): TransactionModel {
        val id = transaction.id
        val type = transaction.type
        val amount = binding!!.edtAmount.text.toString().trim()
        val categoryName = binding!!.tvCategoryName.text.toString().trim()
        val dateTime = binding!!.tvTime.text.toString().trim()
        val note = binding!!.edtDescription.text.toString().trim()
        return TransactionModel(id, type, categoryName, amount.toDouble(), dateTime, note)
    }

    private fun onClickRemoveTransaction() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(status: Boolean) {}

    override fun onSuccess(message: String) {
        activity?.showToast(message)
        hideBottomSheet()
        callback.transactionUpdated(true)
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        bagPresenter.handlerUpdateBalance(
            walletId = wallet.id!!,
            transactionType = transaction.type.toString(),
            currentBalance = wallet.balance!!.toDouble(),
            beforeMoney = transaction.amount!!.toDouble(),
            afterMoney = mNewTransaction.amount!!.toDouble()
        )
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }
}