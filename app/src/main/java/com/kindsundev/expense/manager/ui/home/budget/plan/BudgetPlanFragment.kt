package com.kindsundev.expense.manager.ui.home.budget.plan

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.data.shared.PreferenceHelper
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.create.CreatePlanContract
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.create.CreatePlanDialog
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletContract
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class BudgetPlanFragment : Fragment(), BudgetPlanContract.View {
    private var _binding: FragmentBudgetPlanBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var bottomSheetWallet: BudgetWalletBottomSheet
    private lateinit var mPlanPresenter: BudgetPlanPresenter
    private lateinit var mWallet : WalletModel

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanBinding.inflate(layoutInflater)
        mPlanPresenter = BudgetPlanPresenter(this)
        binding!!.toolbar.inflateMenu(R.menu.plan_manager_menu)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initBottomSheetWallet()
        initListener()
        handleBackNavigation()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {
            it.findNavController().popBackStack()
            toggleBottomNavigation(requireActivity() as HomeActivity, true)
        }
        binding!!.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_select_wallet -> {
                    initBottomSheetWallet()
                    true
                }
                R.id.action_add_plan -> {
                    onClickCreateSpendingPlan()
                    true
                }
                else -> false
            }
        }
    }

    private fun initBottomSheetWallet() {
        bottomSheetWallet = BudgetWalletBottomSheet(
            object : BudgetWalletContract.Listener {
                override fun onClickWalletItem(wallet: WalletModel) {
                    mPlanPresenter.handleGetPlansInWallet(wallet)
                    mWallet = wallet
                    bottomSheetWallet.dismiss()
                }
            })
        bottomSheetWallet.show(parentFragmentManager, bottomSheetWallet.tag)

    }

    override fun onSuccessPlans(planned: ArrayList<PlannedModel>) {
        sharedStatusPlans(planned)
        if (!toggleLayoutEmpty(planned.isEmpty())) {
            binding!!.rcvPlans.apply {
                layoutManager = LinearLayoutManager(getCurrentContext())
                adapter = BudgetPlanAdapter(planned, object : BudgetPlanContract.Listener {
                    override fun onClickPlanItem(planned: PlannedModel) {
                        navigatePlanDetailFragment(planned)
                    }
                })
            }
        }
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun sharedStatusPlans(planned : ArrayList<PlannedModel>) {
        val result = mPlanPresenter.handleExtractionStatusOfPlan(planned)
        if (result.isNotEmpty()) {
            PreferenceHelper.setString(
                getCurrentContext(), Constant.KEY_NOTIFICATION_PLANS + "${mWallet.id}",
                Gson().toJson(result)
            )
        }
    }

    private fun toggleLayoutEmpty(enable: Boolean): Boolean {
        return if (enable) {
            binding!!.rcvPlans.visibility = View.GONE
            binding!!.cvNoPlans.visibility = View.VISIBLE
            true
        } else {
            binding!!.rcvPlans.visibility = View.VISIBLE
            binding!!.cvNoPlans.visibility = View.GONE
            false
        }
    }

    private fun navigatePlanDetailFragment(planned: PlannedModel) {
        findNavController().navigate(
            BudgetPlanFragmentDirections
                .actionBudgetPlanFragmentToBudgetPlanDetailFragment(
                    mWallet,
                    planned.date!!,
                    planned.plan!!
                )
        )
    }

    private fun onClickCreateSpendingPlan() {
        val dialog = CreatePlanDialog(object : CreatePlanContract.Result {
            override fun onSuccessPlan(wallet: WalletModel, plan: PlanModel) {
                mPlanPresenter.handleCreatePlan(wallet.id!!, plan)
                mWallet = wallet
            }
        })
        dialog.show(parentFragmentManager, dialog.tag)
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
        mPlanPresenter.handleGetPlansInFirebase(mWallet.id!!)
    }

    override fun onSuccess() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mPlanPresenter.cleanUp()
    }

    private fun handleBackNavigation() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            toggleBottomNavigation(requireActivity() as HomeActivity, true)
        }
    }
}