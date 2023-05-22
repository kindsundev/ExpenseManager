package com.kindsundev.expense.manager.ui.home.note.transaction

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentTransactionBinding
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.custom.DateTimePickerDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.note.transaction.debt.TransactionDebtBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.debt.TransactionDebtContract
import com.kindsundev.expense.manager.ui.home.note.transaction.plan.TransactionPlanBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.plan.TransactionPlanContract
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.note.transaction.wallet.TransactionWalletContract
import com.kindsundev.expense.manager.utils.*

class TransactionFragment : Fragment(), TransactionContract.View {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding
    private var transactionType: String? = null
    private var categoryName: String? = null

    private val loadingDialog by lazy { LoadingDialog() }
    private lateinit var transactionPresenter: TransactionPresenter

    private lateinit var walletBottomSheet: TransactionWalletBottomSheet
    private lateinit var debtBottomSheet: TransactionDebtBottomSheet
    private lateinit var planBottomSheet: TransactionPlanBottomSheet

    private lateinit var mWallet: WalletModel
    private lateinit var mTransaction: TransactionModel
    private var mPlan: PlanModel? = null
    private lateinit var dateKeyOfPlan: String

    override fun getCurrentContext(): Context = requireContext()

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
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        switchLayout()
        initListener()
        return binding!!.root
    }

    private fun isNotDebtLayout(): Boolean {
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
        debtBottomSheet = TransactionDebtBottomSheet(object : TransactionDebtContract.Listener {
            override fun debtBottomSheetHasDismiss() {
                initWalletBottomSheet()
            }
        })
        debtBottomSheet.show(
            parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_DEBT_NAME
        )
    }

    private fun initWalletBottomSheet() {
        walletBottomSheet =
            TransactionWalletBottomSheet(object : TransactionWalletContract.Listener {
                override fun onClickWalletItem(wallet: WalletModel) {
                    mWallet = wallet
                    binding!!.incomeAndExpense.tvWalletName.text = wallet.name
                    binding!!.debt.tvWalletName.text = wallet.name
                    binding!!.advanced.tvPlan.text = ""
                    binding!!.advanced.tvPlan.hint = getCurrentContext().getString(R.string.plan)
                    mPlan = null
                    walletBottomSheet.dismiss()
                }
            })
        walletBottomSheet.show(
            parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_WALLET_NAME
        )
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { findNavController().popBackStack() }
        binding!!.incomeAndExpense.tvWalletName.setOnClickListener { initWalletBottomSheet() }
        binding!!.incomeAndExpense.itemCategory.setOnClickListener { findNavController().popBackStack() }
        binding!!.incomeAndExpense.itemTime.setOnClickListener { onClickSetDateTime() }

        binding!!.debt.itemPerson.setOnClickListener { initDebtBottomSheet() }
        binding!!.debt.itemTime.setOnClickListener { onClickSetDateTime() }
        binding!!.debt.itemDueDate.setOnClickListener { onClickSetDueDate() }
        binding!!.debt.itemRemindDate.setOnClickListener { onClickSetRemindDate() }

        binding!!.advanced.itemPlan.setOnClickListener { initPlanBottomSheet() }
        binding!!.advanced.itemEvent.setOnClickListener { activity?.requestPremium() }
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
            val amountText = binding!!.incomeAndExpense.edtAmount.text.toString()
            val walletNameHint = binding!!.incomeAndExpense.tvWalletName.hint.toString()
            val walletNameText = binding!!.incomeAndExpense.tvWalletName.text.toString()
            if (transactionPresenter.isDataFromInputValid(
                    amountText,
                    walletNameHint,
                    walletNameText
                )
            ) {
                mTransaction = initTransactionDataInIAE()
                transactionPresenter.createTransaction(mWallet.id!!, mTransaction)
            }
        } else {
            activity?.requestPremium()
        }
    }

    private fun initTransactionDataInIAE(): TransactionModel {
        val amount = binding!!.incomeAndExpense.edtAmount.text.toString().trim().replace(",", "")
        val date = binding!!.incomeAndExpense.tvTime.text.toString().trim()
        val note = binding!!.incomeAndExpense.edtDescription.text.toString().trim()
        val id = hashCodeForID(transactionType!!, categoryName!!, date, note)
        val planId = mPlan?.id
        return TransactionModel(
            id, transactionType!!, categoryName!!, amount.toDouble(), date, note, planId
        )
    }

    private fun initPlanBottomSheet() {
        if (binding!!.incomeAndExpense.tvWalletName.text.isEmpty()) {
            activity?.showMessage(
                getCurrentContext().getString(R.string.please_select_wallet_before)
            )
        } else {
            planBottomSheet =
                TransactionPlanBottomSheet(mWallet, object : TransactionPlanContract.Listener {
                    override fun onClickPlanItem(planned: PlannedModel) {
                        mPlan = planned.plan
                        dateKeyOfPlan = planned.date!!
                        binding!!.advanced.tvPlan.text = mPlan!!.name
                        binding!!.advanced.tvPlan.setTextColor(Color.BLACK)
                        planBottomSheet.dismiss()
                    }
                })
            planBottomSheet.show(
                parentFragmentManager,
                Constant.TRANSACTION_WALLET_BOTTOM_SHEET_PLAN_NAME
            )
        }
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onShowMessage(message: String) {
        activity?.showMessage(message)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess(message: String) {
        activity?.showMessage(message)
        if (mTransaction.planId != null) {
            val amount =
                binding!!.incomeAndExpense.edtAmount.text.toString().trim().replace(",", "")
            transactionPresenter.handleUpdateBalanceOfPlan(
                mWallet.id!!,
                dateKeyOfPlan,
                mPlan!!.id!!,
                transactionType!!,
                mPlan!!.currentBalance!!,
                amount.toDouble()
            )
        }
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        transactionPresenter.handleUpdateBalanceOfWallet(
            mWallet.id!!, transactionType!!, mWallet.balance!!.toDouble(),
            mTransaction.amount!!
        )
    }

    override fun onSuccessPlan() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
        transactionPresenter.cleanUp()
    }
}