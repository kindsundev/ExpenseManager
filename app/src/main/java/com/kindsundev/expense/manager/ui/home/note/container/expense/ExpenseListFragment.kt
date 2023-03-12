package com.kindsundev.expense.manager.ui.home.note.container.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.databinding.FragmentExpenseListBinding

class ExpenseListFragment : Fragment() {
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(layoutInflater, container, false)
        initListener()
        return binding!!.root
    }
    
    private fun initListener() {
        binding!!.itemEat.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemBill.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemMove.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemHouse.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemShopping.setOnClickListener { startCreateTransaction(getContentDescription(it)) }

        binding!!.itemEntertainment.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemOutfit.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemTravel.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemBeautify.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemParty.setOnClickListener { startCreateTransaction(getContentDescription(it)) }

        binding!!.itemGift.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemCharity.setOnClickListener { startCreateTransaction(getContentDescription(it)) }

        binding!!.itemDoctor.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemSport.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemInsurance.setOnClickListener { startCreateTransaction(getContentDescription(it)) }

        binding!!.itemChild.setOnClickListener { startCreateTransaction(getContentDescription(it)) }

        binding!!.itemFees.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.itemDrop.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
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