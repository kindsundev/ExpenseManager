package com.kindsundev.expense.manager.ui.home.note.transaction.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetSelectPlanBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class TransactionPlanBottomSheet(
    private val wallet: WalletModel,
    private val listener: TransactionPlanContract.Listener
): BottomSheetDialogFragment(), TransactionPlanContract.View{
    private var _binding: BottomSheetSelectPlanBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var planPresenter: TransactionPlanPresenter

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectPlanBinding.inflate(inflater)
        planPresenter = TransactionPlanPresenter(this)
        planPresenter.handleGetPlans(wallet)
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
        return binding!!.root
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {}

    override fun onSuccessPlans(plans: ArrayList<PlannedModel>) {
        binding!!.rvPlans.apply {
            layoutManager = LinearLayoutManager(getCurrentContext())
            adapter = TransactionPlanAdapter(plans, listener)
        }
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}