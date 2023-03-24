package com.kindsundev.expense.manager.ui.home.note.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.shared.PreferenceHelper
import com.kindsundev.expense.manager.databinding.FragmentNoteBinding
import com.kindsundev.expense.manager.ui.home.note.container.debt.DebtListFragment
import com.kindsundev.expense.manager.ui.home.note.container.expense.ExpenseListFragment
import com.kindsundev.expense.manager.ui.home.note.container.income.IncomeListFragment
import kotlin.properties.Delegates

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding

    private lateinit var expenseFragment: ExpenseListFragment
    private lateinit var incomeFragment: IncomeListFragment
    private lateinit var debtFragment: DebtListFragment

    private var beforeCheckedId by Delegates.notNull<Int>()

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
        getCurrentButtonChecked()
        initDefaultList()
        initListener()
        return binding!!.root
    }

    private fun getCurrentButtonChecked() {
        beforeCheckedId = PreferenceHelper.getInteger(
            requireContext(), Constant.NOTE_FRAGMENT_BUTTON_LIST_STATUS, 0
        )
        if (beforeCheckedId == 0) {
            binding!!.btnExpense.isChecked = true
        } else {
            when (beforeCheckedId) {
                binding!!.btnExpense.id -> {
                    binding!!.btnExpense.isChecked = true
                }
                binding!!.btnIncome.id -> {
                    binding!!.btnIncome.isChecked = true
                }
                binding!!.btnDebt.id -> {
                    binding!!.btnDebt.isChecked = true
                }
                else -> {}
            }
        }
    }

    private fun initDefaultList() {
        when (beforeCheckedId) {
            binding!!.btnExpense.id -> addFragment(expenseFragment)
            binding!!.btnIncome.id -> addFragment(incomeFragment)
            binding!!.btnDebt.id -> addFragment(debtFragment)
            else -> {}
        }
    }

    private fun addFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.fl_list, fragment, tag)
            .commit()
    }

    /*
    * Why i don't use navigation navigation but replace fragment?
    * => Because if using navigate it will change full screen,
    *    but what i want is to change the frame in that screen
    * */
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

    override fun onStop() {
        super.onStop()
        saveRadioButtonState()
    }

    private fun saveRadioButtonState() {
        val currentValue = binding!!.toggle.checkedRadioButtonId
        PreferenceHelper.setInteger(
            requireContext(), Constant.NOTE_FRAGMENT_BUTTON_LIST_STATUS, currentValue
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragments()
        _binding = null
    }

    private fun clearFragments() {
        parentFragmentManager.beginTransaction().remove(expenseFragment).commit()
        parentFragmentManager.beginTransaction().remove(incomeFragment).commit()
        parentFragmentManager.beginTransaction().remove(debtFragment).commit()
    }
}