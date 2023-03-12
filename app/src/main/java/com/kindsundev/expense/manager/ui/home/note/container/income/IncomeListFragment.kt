package com.kindsundev.expense.manager.ui.home.note.container.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.databinding.FragmentIcomeListBinding

class IncomeListFragment : Fragment() {
    private var _binding: FragmentIcomeListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIcomeListBinding.inflate(layoutInflater, container, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.itemSalary.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemBonus.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemInterestRate.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemOther.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun getContentDescription(view: View): String {
        return view.contentDescription.toString()
    }

    private fun startCreateTransaction(content: String) {
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}