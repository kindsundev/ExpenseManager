package com.kindsundev.expense.manager.ui.home.bag.detail

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetTransactionDetailBinding
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.custom.DateTimePickerDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.*

class TransactionBottomSheet(
    private val callback: TransactionDetailContract.Result,
    private val wallet: WalletModel,
    private val date: String,
    private val transaction: TransactionModel
) : BottomSheetDialogFragment(), TransactionDetailContract.View {
    private var _binding: BottomSheetTransactionDetailBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var detailPresenter: TransactionDetailPresenter
    private lateinit var mPlanned: PlannedModel

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailPresenter = TransactionDetailPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTransactionDetailBinding.inflate(layoutInflater)
        formatInputCurrencyBalance(binding!!.edtAmount)
        initTransactionData()
        detailPresenter.handleExtractionPlan(wallet, transaction.planId)
        initListener()
        return binding!!.root
    }

    override fun onExtractionPlanSuccess(planned: PlannedModel) {
        mPlanned = planned
    }

    private fun initTransactionData() {
        val amount = formatDisplayCurrencyBalance(transaction.amount.toString()).trim()
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
        binding!!.btnRemove.setOnClickListener { onClickDeleteTransaction() }
    }

    private fun hideBottomSheet(): Unit = this.dismiss()

    private fun onClickSetDateTime() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvTime.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickUpdateTransaction() {
        val message: String
        if (binding!!.edtAmount.text.isEmpty()) {
            message = getCurrentContext().getString(R.string.please_enter_amount)
            activity?.showMessage(message)
        } else {
            val oldValue = transaction.amount
            val newValue = binding!!.edtAmount.text.toString().replace(",", "").toDouble()
            if (oldValue == newValue) {
                message = getCurrentContext().getString(R.string.do_not_enter_old_money)
                activity?.showMessage(message)
            } else {
                val mNewTransaction = getNewTransaction()
                detailPresenter.updateTransaction(wallet.id!!, date, mNewTransaction)
            }
        }
    }

    private fun getNewTransaction(): TransactionModel {
        val id = transaction.id
        val type = transaction.type
        val amount = binding!!.edtAmount.text.toString().trim().replace(",", "")
        val categoryName = binding!!.tvCategoryName.text.toString().trim()
        val dateTime = binding!!.tvTime.text.toString().trim()
        val note = binding!!.edtDescription.text.toString().trim()
        val planId = transaction.planId
        return TransactionModel(id, type, categoryName, amount.toDouble(), dateTime, note, planId)
    }

    private fun onClickDeleteTransaction() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmAlertDialog)
            .setTitle(R.string.delete_transaction)
            .setMessage(R.string.message_for_delete_transaction)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                detailPresenter.handleDeleteTransaction(wallet.id!!, date, transaction.id!!)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        alertDialog.show()
    }

    override fun onSuccess() {}

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onUpdateTransactionSuccess(message: String) {
        activity?.showMessage(message)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        detailPresenter.handleUpdateBalanceOfWallet(
            walletId = wallet.id!!,
            transactionType = transaction.type.toString(),
            currentBalance = wallet.balance!!.toDouble(),
            beforeMoney = transaction.amount!!.toDouble(),
            afterMoney = getNewAmount()
        )
    }

    override fun onUpdateBalanceWalletSuccess(isDeleteAction: Boolean) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        callback.onActionSuccess()
        hideBottomSheet()
        // check update transaction have plan
        if (transaction.planId != null) {
            if (isDeleteAction) {
                requestCheckAndRestoreBalance(true)
            } else {
                detailPresenter.handleUpdateBalanceOfPlan(
                    walletId = wallet.id!!,
                    dateKey = mPlanned.date!!,
                    planId = transaction.planId,
                    transactionType = transaction.type!!,
                    currentBalance = mPlanned.plan!!.currentBalance!!,
                    beforeMoney = transaction.amount!!.toDouble(),
                    afterMoney = getNewAmount()
                )
            }
        }
    }

    private fun requestCheckAndRestoreBalance(ownPlan: Boolean) {
        if (ownPlan) {
            detailPresenter.checkAndRestoreBalance(
                walletId = wallet.id!!,
                transactionType = transaction.type!!,
                currentBalance = mPlanned.plan!!.currentBalance!!,
                beforeMoney = transaction.amount!!.toDouble(),
                ownPlan = true,
                mPlanned.date!!,
                transaction.planId
            )
        } else {
            detailPresenter.checkAndRestoreBalance(
                walletId = wallet.id!!,
                transactionType = transaction.type!!,
                currentBalance = wallet.balance!!,
                beforeMoney = transaction.amount!!.toDouble(),
                ownPlan = false,
            )
        }

    }

    override fun onDeleteTransactionSuccess(message: String) {
        activity?.showMessage(message)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        requestCheckAndRestoreBalance(false)
    }

    override fun onUpdateBalancePlanSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun getNewAmount(): Double {
        return binding!!.edtAmount.text.toString().trim().replace(",", "").toDouble()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        detailPresenter.cleanUp()
    }
}