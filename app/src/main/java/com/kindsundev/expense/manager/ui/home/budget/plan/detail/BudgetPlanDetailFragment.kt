package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanDetailBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillAdapterContract
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillParentAdapter
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionBottomSheet
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionDetailContract
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update.UpdatePlanContract
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update.UpdatePlanDialog
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class BudgetPlanDetailFragment : Fragment(), BudgetPlanDetailContract.View {
    private var _binding: FragmentBudgetPlanDetailBinding? = null
    private val binding get() = _binding
    private var isLayoutInfoVisible = true
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var planDetailPresenter: BudgetPlanDetailPresenter
    private val args by navArgs<BudgetPlanDetailFragmentArgs>()
    private lateinit var mWallet : WalletModel
    private lateinit var mPlan: PlanModel
    private lateinit var mDate: String

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanDetailBinding.inflate(layoutInflater)
        binding!!.toolbar.inflateMenu(R.menu.plan_detail_menu)
        planDetailPresenter = BudgetPlanDetailPresenter(this)
        getDataFromBudgetPlan()
        initPlanData()
        planDetailPresenter.handleExtractionBills(mWallet, mPlan.id!!)
        initListener()
        return binding!!.root
    }

    private fun getDataFromBudgetPlan() {
        mPlan = args.plan
        mWallet = args.wallet
        mDate = args.date
    }

    private fun initPlanData() {
        binding!!.tvName.text = mPlan.name
        binding!!.tvCurrentBalance.text = formatDisplayCurrencyBalance(mPlan.currentBalance.toString())
        binding!!.tvEstimatedAmount.text = formatDisplayCurrencyBalance(mPlan.estimatedAmount.toString())

        val startDay = mPlan.startDate!!.split(", ")
        val endDay = mPlan.endDate!!.split(", ")
        binding!!.tvStartDate.text = startDay[1]
        binding!!.tvEndDate.text = endDay[1]

        val percentage = (mPlan.currentBalance!!.toDouble() / mPlan.estimatedAmount!!.toDouble()) * 100
        binding!!.progressBar.progress = percentage.toInt()
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {it.findNavController().popBackStack() }
        binding!!.iBtnVisibility.setOnClickListener { toggleLayoutInfo()}
        binding!!.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_update_plan -> {
                    initUpdatePlanDialog()
                    true
                }
                R.id.action_delete_plan -> {
                    initDeletePlanDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun toggleLayoutInfo() {
        if (isLayoutInfoVisible) {
            binding!!.iBtnVisibility.setImageResource(R.drawable.ic_visibility)
            binding!!.cardViewDetail.visibility = View.GONE
            isLayoutInfoVisible = false
        } else {
            binding!!.iBtnVisibility.setImageResource(R.drawable.ic_visibility_off)
            binding!!.cardViewDetail.visibility = View.VISIBLE
            isLayoutInfoVisible = true
        }
    }

    private fun initUpdatePlanDialog() {
        val dialog = UpdatePlanDialog(mWallet.id!!, mDate, mPlan, object: UpdatePlanContract.Listener{
            override fun requestUpdateData(walletId: Int, dateKey: String, planId: Int) {
                planDetailPresenter.handleGetPlan(walletId, dateKey, planId)
            }
        })
        dialog.show(parentFragmentManager, dialog.tag)
    }

    private fun initDeletePlanDialog() {
        val alertDialog = MaterialAlertDialogBuilder(getCurrentContext(), R.style.ConfirmAlertDialog)
            .setTitle(R.string.delete_plan)
            .setMessage(R.string.confirm_delete_plan)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                planDetailPresenter.handleDeletePlan(mWallet.id!!, mDate, mPlan.id!!)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        alertDialog.show()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(getCurrentContext().getString(R.string.delete_plan_success))
        findNavController().popBackStack()
    }

    override fun onSuccessPlan(plan: PlanModel) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        mPlan = plan
        initPlanData()
    }

    override fun onSuccessBill(bills: ArrayList<BillModel>, isRequestPlan: Boolean) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        initRecyclerView(bills)
        if (isRequestPlan) {
            planDetailPresenter.handleGetPlan(mWallet.id!!, mDate, mPlan.id!!)
        }
    }

    private fun initRecyclerView(bills: ArrayList<BillModel>) {
        binding!!.rcvBills.apply {
            layoutManager = LinearLayoutManager(getCurrentContext())
            adapter = BillParentAdapter(bills, object: BillAdapterContract.Listener {
                override fun onClickTransaction(date: String, transaction: TransactionModel) {
                    initTransactionBottomSheet(date, transaction)
                }
            })
        }
    }

    private fun initTransactionBottomSheet(date: String, transaction: TransactionModel) {
        val bottomSheet = TransactionBottomSheet(object : TransactionDetailContract.Result {
            override fun onActionSuccess() {
                planDetailPresenter.handleGetBills(mWallet.id!!, mPlan.id!!, true)
            }
        }, mWallet, date, transaction)
        bottomSheet.show(parentFragmentManager, Constant.TRANSACTION_WALLET_BOTTOM_SHEET_TRANSACTION_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        planDetailPresenter.cleanUp()
    }
}