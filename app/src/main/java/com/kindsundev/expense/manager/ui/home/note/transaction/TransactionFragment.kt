package com.kindsundev.expense.manager.ui.home.note.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentTransactionBinding
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.custom.DateTimePickerDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletContract
import com.kindsundev.expense.manager.utils.*

class TransactionFragment : Fragment(),
    TransactionWalletContract.Listener, TransactionContract.View {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding

    private lateinit var transactionWalletBottomSheet: TransactionWalletBottomSheet
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
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, false)
        formatInputCurrencyBalance(binding!!.main.edtAmount)
        initCurrentData()
        initWalletBottomSheet()
        initListener()
        return binding!!.root
    }

    private fun initCurrentData() {
        binding!!.main.tvCategoryName.text = categoryName
        binding!!.main.tvTime.text = getCurrentDateTime()
    }

    private fun initWalletBottomSheet() {
        transactionWalletBottomSheet = TransactionWalletBottomSheet(this)
        transactionWalletBottomSheet.show(parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    override fun onClickWalletItem(wallet: WalletModel) {
        this.wallet = wallet
        binding!!.main.tvWalletName.text = wallet.name
        transactionWalletBottomSheet.hideBottomSheet()
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { findNavController().popBackStack() }
        binding!!.main.tvWalletName.setOnClickListener { initWalletBottomSheet() }
        binding!!.main.itemCategory.setOnClickListener { findNavController().popBackStack() }
        binding!!.main.itemTime.setOnClickListener { onClickSetDateTime() }
        binding!!.advanced.itemEvent.setOnClickListener { activity?.requestPremium() }
        binding!!.advanced.switchOptimization.setOnClickListener { activity?.requestPremium() }
        binding!!.advanced.tvShowMore.setOnClickListener { activity?.requestPremium() }
        binding!!.confirm.btnSave.setOnClickListener { onClickSave() }
    }

    private fun initTransactionData(): TransactionModel {
        val amount = binding!!.main.edtAmount.text.toString().trim().replace(",","")
        val date = binding!!.main.tvTime.text.toString().trim()
        val note = binding!!.main.edtDescription.text.toString().trim()
        val id = hashCodeForID(transactionType!!, categoryName!!, date, note)
        return TransactionModel(
            id, transactionType!!, categoryName!!, amount.toDouble(), date, note
        )
    }

    private fun onClickSetDateTime() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.main.tvTime.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickSave() {
        if (binding!!.main.edtAmount.text.isEmpty()) {
            activity?.showToast("Please enter your amount")
        } else {
            transaction = initTransactionData()
            transactionPresenter.createTransaction(wallet.id!!, transaction)
        }
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
        transactionPresenter.handlerUpdateBalance(
            wallet.id!!, transactionType!!, wallet.balance!!.toDouble(),
            transaction.amount!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
        transactionPresenter.cleanUp()
    }
}