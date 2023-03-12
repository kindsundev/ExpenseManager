package com.kindsundev.expense.manager.ui.home.note.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.FragmentNoteBinding
import com.kindsundev.expense.manager.ui.home.note.container.debt.DebtListFragment
import com.kindsundev.expense.manager.ui.home.note.container.expense.ExpenseListFragment
import com.kindsundev.expense.manager.ui.home.note.container.income.IncomeListFragment

class NoteFragment : Fragment() {
    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding

    private lateinit var expenseFragment: ExpenseListFragment
    private lateinit var incomeFragment: IncomeListFragment
    private lateinit var debtFragment: DebtListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expenseFragment = ExpenseListFragment()
        incomeFragment = IncomeListFragment()
        debtFragment = DebtListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        initDefaultList()
        initListener()
        return binding!!.root
    }

    private fun initDefaultList() {
        when(binding!!.toggle.checkedRadioButtonId) {
            binding!!.btnExpense.id -> addFragment(expenseFragment)
            binding!!.btnIncome.id -> addFragment(incomeFragment)
            binding!!.btnDebt.id -> addFragment(debtFragment)
            else -> {}
        }
    }

    private fun addFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.fl_list, fragment)
            .commit()
    }

    private fun initListener() {
        binding!!.btnExpense.setOnClickListener { replaceFragment(expenseFragment) }
        binding!!.btnIncome.setOnClickListener { replaceFragment(incomeFragment) }
        binding!!.btnDebt.setOnClickListener { replaceFragment(debtFragment) }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_list, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding!!.toggle.clearCheck()
        _binding = null
    }
}