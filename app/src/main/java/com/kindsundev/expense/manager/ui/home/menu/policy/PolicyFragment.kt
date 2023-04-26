package com.kindsundev.expense.manager.ui.home.menu.policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.FragmentPolicyBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class PolicyFragment : Fragment() {
    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPolicyBinding.inflate(inflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initGeneralTermsData()
        initListener()
        return binding!!.root
    }

    private fun initGeneralTermsData() {
        binding!!.tvLine1.setText(R.string.general_terms_child_line_1)
        binding!!.tvLine2.setText(R.string.general_terms_child_line_2)
        binding!!.tvLine3.setText(R.string.general_terms_child_line_3)
        binding!!.tvLine4.setText(R.string.general_terms_child_line_4)
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding!!.btnGeneralTerms.setOnClickListener {
            initGeneralTermsData()
        }
        binding!!.btnDataManagement.setOnClickListener {
            initDataManagementData()
        }
    }

    private fun initDataManagementData() {
        binding!!.tvLine1.setText(R.string.data_management_child_line_1)
        binding!!.tvLine2.setText(R.string.data_management_child_line_2)
        binding!!.tvLine3.setText(R.string.data_management_child_line_3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}