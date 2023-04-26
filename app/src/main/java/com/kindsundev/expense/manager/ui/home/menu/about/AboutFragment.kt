package com.kindsundev.expense.manager.ui.home.menu.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.databinding.FragmentAboutBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.displaySwitchBottomNavigation

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(layoutInflater)
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, false)
        binding!!.btnBack.setOnClickListener { it.findNavController().popBackStack() }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}