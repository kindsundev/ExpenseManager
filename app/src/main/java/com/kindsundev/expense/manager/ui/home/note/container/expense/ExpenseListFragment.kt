package com.kindsundev.expense.manager.ui.home.note.container.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
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
        initNeedFulListener()
        initEnjoyListener()
        initOfferingListener()
        initHealthListener()
        initOtherListener()
        binding!!.child.itemChild.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun initNeedFulListener() {
        binding!!.needful.itemEat.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.needful.itemBill.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.needful.itemMove.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.needful.itemHouse.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.needful.itemShopping.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun initEnjoyListener() {
        binding!!.enjoy.itemEntertainment.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.enjoy.itemOutfit.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.enjoy.itemTravel.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.enjoy.itemBeautify.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.enjoy.itemParty.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun initOfferingListener() {
        binding!!.offering.itemGift.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.offering.itemCharity.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun initHealthListener() {
        binding!!.health.itemDoctor.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.health.itemSport.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.health.itemInsurance.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun initOtherListener() {
        binding!!.other.itemFees.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
        binding!!.other.itemDrop.setOnClickListener { startCreateTransaction(getContentDescription(it)) }
    }

    private fun getContentDescription(view: View): String {
        return view.contentDescription.toString()
    }

    private fun startCreateTransaction(content: String) {
        val nameAndType = ArrayList<String>()
        nameAndType.add(content)
        nameAndType.add("Expense")
        val bundle = bundleOf(Constant.CATEGORY_TRANSACTION_NAME to nameAndType)
        findNavController().navigate(R.id.transactionFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}