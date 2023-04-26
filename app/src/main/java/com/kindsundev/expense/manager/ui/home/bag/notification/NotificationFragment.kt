package com.kindsundev.expense.manager.ui.home.bag.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.databinding.FragmentNotificationBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { it.findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}