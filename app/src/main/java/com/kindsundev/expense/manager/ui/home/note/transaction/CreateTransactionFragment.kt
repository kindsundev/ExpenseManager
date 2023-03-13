package com.kindsundev.expense.manager.ui.home.note.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.databinding.BottomsheetCreateTransactionBinding

class CreateTransactionFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetCreateTransactionBinding? = null
    private val binding get() = _binding
    private var categoryName: String? = null

    companion object {
        fun newInstance(name: String): CreateTransactionFragment {
            val transactionFragment by lazy { CreateTransactionFragment() }
            val bundle by lazy { Bundle() }
            bundle.putString(Constant.CATEGORY_TRANSACTION_NAME, name)
            transactionFragment.arguments = bundle
            return transactionFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = requireArguments()
                .getString(Constant.CATEGORY_TRANSACTION_NAME, "Null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetCreateTransactionBinding.inflate(layoutInflater)
        binding!!.tvCategoryName.text = categoryName.toString()
        Logger.error(categoryName.toString())
        return binding!!.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}