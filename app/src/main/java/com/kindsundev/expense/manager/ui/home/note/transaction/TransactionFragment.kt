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
import com.kindsundev.expense.manager.ui.home.note.transaction.debt.TransactionDebtBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.debt.TransactionDebtContract
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletContract
import com.kindsundev.expense.manager.utils.*

class TransactionFragment : Fragment(), TransactionContract.View,
    TransactionWalletContract.Listener, TransactionDebtContract.Listener {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding

    private lateinit var walletBottomSheet: TransactionWalletBottomSheet
    private lateinit var debtBottomSheet: TransactionDebtBottomSheet
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
        switchLayout()
        initListener()
        return binding!!.root
    }

    private fun isNotDebtLayout() : Boolean {
        if (transactionType == Constant.TRANSACTION_TYPE_DEBT) {
            return false
        }
        return true
    }

    private fun switchLayout() {
        if (isNotDebtLayout()) {
            binding!!.debt.root.visibility = View.GONE
            binding!!.incomeAndExpense.root.visibility = View.VISIBLE
            formatInputCurrencyBalance(binding!!.incomeAndExpense.edtAmount)
            initCurrentData()
            initWalletBottomSheet()
        } else {
            binding!!.incomeAndExpense.root.visibility = View.GONE
            binding!!.debt.root.visibility = View.VISIBLE
            formatInputCurrencyBalance(binding!!.debt.edtAmount)
            initCurrentData()
            initDebtBottomSheet()
        }
    }

    private fun initCurrentData() {
        if (isNotDebtLayout()) {
            binding!!.incomeAndExpense.tvCategoryName.text = categoryName
            binding!!.incomeAndExpense.tvTime.text = getCurrentDateTime()
        } else {
            binding!!.debt.tvCategoryName.text = categoryName
            binding!!.debt.tvTime.text = getCurrentDateTime()
        }
    }

    private fun initDebtBottomSheet() {
        debtBottomSheet = TransactionDebtBottomSheet(this)
        debtBottomSheet.show(parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_DEBT_NAME)
    }

    override fun debtBottomSheetHasDismiss() {
        initWalletBottomSheet()
    }

    private fun initWalletBottomSheet() {
        walletBottomSheet = TransactionWalletBottomSheet(this)
        walletBottomSheet.show(parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    override fun onClickWalletItem(wallet: WalletModel) {
        this.wallet = wallet
        binding!!.incomeAndExpense.tvWalletName.text = wallet.name
        binding!!.debt.tvWalletName.text = wallet.name
        walletBottomSheet.dismiss()
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { findNavController().popBackStack() }
        binding!!.incomeAndExpense.tvWalletName.setOnClickListener { initWalletBottomSheet() }
        binding!!.incomeAndExpense.itemCategory.setOnClickListener { findNavController().popBackStack() }
        binding!!.incomeAndExpense.itemTime.setOnClickListener { onClickSetDateTime() }

        binding!!.debt.itemPerson.setOnClickListener { initDebtBottomSheet() }
        binding!!.debt.itemTime.setOnClickListener { onClickSetDateTime() }
        binding!!.debt.itemDueDate.setOnClickListener { onClickSetDueDate()}
        binding!!.debt.itemRemindDate.setOnClickListener { onClickSetRemindDate() }

        binding!!.advanced.itemEvent.setOnClickListener { activity?.requestPremium() }
        binding!!.advanced.switchOptimization.setOnClickListener { activity?.requestPremium() }
        binding!!.advanced.tvShowMore.setOnClickListener { activity?.requestPremium() }

        binding!!.confirm.btnSave.setOnClickListener { onClickSave() }
    }

    private fun onClickSetDateTime() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                if (isNotDebtLayout()) {
                    binding!!.incomeAndExpense.tvTime.text = newDateTime
                } else {
                    binding!!.debt.tvTime.text = newDateTime
                }
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickSetDueDate() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.debt.tvDueDate.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickSetRemindDate() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.debt.tvRemindDate.text = newDateTime
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickSave() {
        if (isNotDebtLayout()) {
            if (binding!!.incomeAndExpense.edtAmount.text.isEmpty()) {
                activity?.showToast("Please enter your amount")
            } else {
                transaction = initTransactionDataInIAE()
                transactionPresenter.createTransaction(wallet.id!!, transaction)
            }
        } else {
            activity?.requestPremium()
        }
    }

    private fun initTransactionDataInIAE(): TransactionModel {
        val amount = binding!!.incomeAndExpense.edtAmount.text.toString().trim().replace(",","")
        val date = binding!!.incomeAndExpense.tvTime.text.toString().trim()
        val note = binding!!.incomeAndExpense.edtDescription.text.toString().trim()
        val id = hashCodeForID(transactionType!!, categoryName!!, date, note)
        return TransactionModel(
            id, transactionType!!, categoryName!!, amount.toDouble(), date, note
        )
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