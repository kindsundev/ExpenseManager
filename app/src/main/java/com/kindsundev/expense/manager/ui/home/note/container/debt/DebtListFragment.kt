package com.kindsundev.expense.manager.ui.home.note.container.debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentDebtListBinding

class DebtListFragment : Fragment() {
    private var _binding: FragmentDebtListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebtListBinding.inflate(layoutInflater, container, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.itemLend.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemRepayment.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemBorrow.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemDebtCollection.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun getContentDescription(view: View): String {
        return view.contentDescription.toString()
    }

    private fun startCreateTransaction(content: String) {
        val nameAndType = ArrayList<String>()
        nameAndType.add(content)
        nameAndType.add(Constant.TRANSACTION_TYPE_DEBT)
        val bundle = bundleOf(Constant.CATEGORY_TRANSACTION_NAME to nameAndType)
        findNavController().navigate(R.id.transactionFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}