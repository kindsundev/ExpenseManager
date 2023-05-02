package com.kindsundev.expense.manager.ui.home.budget.plan

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.CreatePlanContract
import com.kindsundev.expense.manager.ui.home.budget.plan.dialog.CreatePlanDialog
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletContract
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class BudgetPlanFragment : Fragment(){
    private var _binding: FragmentBudgetPlanBinding? = null
    private val binding get() = _binding
    private lateinit var toolbar: Toolbar

    private lateinit var bottomSheetWallet: BudgetWalletBottomSheet
    private lateinit var mCurrentWallet: WalletModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanBinding.inflate(layoutInflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initToolbar()
        initBottomSheetWallet()
        initListener()
        return binding!!.root
    }

    private fun initToolbar() {
        toolbar = binding!!.toolbar
        toolbar.inflateMenu(R.menu.plan_manager_menu)
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {it.findNavController().popBackStack() }
        toolbar.setOnMenuItemClickListener { menuItem ->
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
        bottomSheetWallet = BudgetWalletBottomSheet(object: BudgetWalletContract.Listener {
            override fun onClickWalletItem(wallet: WalletModel) {
                mCurrentWallet = wallet
                bottomSheetWallet.dismiss()
            }
        })
        bottomSheetWallet.show(parentFragmentManager, bottomSheetWallet.tag)
    }

    private fun onClickCreateSpendingPlan() {
        val dialog = CreatePlanDialog(object: CreatePlanContract.Result {
            override fun onSuccessPlan(wallet: WalletModel, plan: PlanModel) {
                mCurrentWallet = wallet
                Logger.error(wallet.toString())
                Logger.warn(plan.toString())
                // call presenter add to database is here
            }
        })
        dialog.show(parentFragmentManager, dialog.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
    }
}