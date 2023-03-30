package com.kindsundev.expense.manager.ui.home.note.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentTransactionBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletContract
import com.kindsundev.expense.manager.utils.*

class TransactionFragment : Fragment(),
    WalletContract.Listener, TransactionContract.View {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding

    private lateinit var walletBottomSheet: WalletBottomSheet
    private lateinit var transactionPresenter: TransactionPresenter
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var wallet: WalletModel
    private lateinit var transaction: TransactionModel
    private var transactionType: String? = null
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageReceived = arguments?.getStringArrayList(Constant.CATEGORY_TRANSACTION_NAME)
        packageReceived?.let {
            categoryName = packageReceived[0]
            transactionType = packageReceived[1]
        }
        transactionPresenter = TransactionPresenter(this)
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
        binding!!.tvTime.text = getCurrentDate()
    }

    private fun initWalletBottomSheet() {
        walletBottomSheet = WalletBottomSheet(this)
        walletBottomSheet.show(parentFragmentManager, Constant.WALLET_BOTTOM_SHEET_NAME)
    }

    override fun onClickWalletItem(wallet: WalletModel) {
        this.wallet = wallet
        binding!!.tvTransactionName.text = wallet.name
        walletBottomSheet.hideBottomSheet()
    }

    private fun initListener() {
        binding!!.tvTransactionName.setOnClickListener { initWalletBottomSheet() }
        binding!!.itemCategory.setOnClickListener { findNavController().popBackStack() }
        binding!!.itemEvent.setOnClickListener { activity?.requestPremium() }
        binding!!.switchOptimization.setOnClickListener { activity?.requestPremium() }
        binding!!.tvShowMore.setOnClickListener { activity?.requestPremium() }
        binding!!.btnSave.setOnClickListener { onClickSave() }
        binding!!.btnArrowDown.setOnClickListener { onClickArrowDown() }
    }

    private fun onClickSave() {
        if (binding!!.edtAmount.text.isEmpty()) {
            activity?.showToast("Please enter your amount")
        } else {
            transaction = initTransactionData()
            transactionPresenter.createTransaction(wallet.id!!, transaction)
        }
    }

    private fun  onClickArrowDown() {
        findNavController().popBackStack()
    }

    private fun initTransactionData(): TransactionModel {
        val amount = binding!!.edtAmount.text.toString().trim()
        val date = binding!!.tvTime.text.toString().trim()
        val note = binding!!.edtDescription.text.toString().trim()
        val id = hashCodeForID(transactionType!!, categoryName!!, date, note)
        return TransactionModel(
            id, transactionType!!, categoryName!!, amount.toDouble(), date, note
        )
    }

    private fun handlerUpdateBalance(amount: Double?) {
        transactionPresenter.handlerUpdateBalance(
            wallet.id!!, transactionType!!, wallet.balance!!.toDouble(),
            amount!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionPresenter.cleanUp()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess(message: String) {
        activity?.showToast(message)
        findNavController().popBackStack()
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        handlerUpdateBalance(transaction.amount)
    }
}