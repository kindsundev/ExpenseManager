package com.kindsundev.expense.manager.ui.home.menu.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.FragmentFeedbackBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.toggleBottomNavigation
import com.kindsundev.expense.manager.utils.showMessage

class FeedbackFragment: Fragment() {
    private var _binding : FragmentFeedbackBinding? = null
    private val binding get() = _binding
    private val clickedButtons = mutableListOf<Button>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding!!.btnSend.setOnClickListener {
            activity?.showMessage(requireContext().getString(R.string.thank_for_feedback))
            it.findNavController().popBackStack()
        }
        binding!!.btnTransparency.setOnClickListener { toggleButtonBackground(it as Button) }
        binding!!.btnCustomerSupport.setOnClickListener { toggleButtonBackground(it as Button) }
        binding!!.btnOverallService.setOnClickListener { toggleButtonBackground(it as Button) }
        binding!!.btnIncorrectCalculation.setOnClickListener { toggleButtonBackground(it as Button) }
    }

    private fun toggleButtonBackground(button: Button) {
        if (clickedButtons.contains(button)) {
            button.setBackgroundResource(R.drawable.btn_gray_border_normal)
            clickedButtons.remove(button)
        } else {
            button.setBackgroundResource(R.drawable.btn_border_yellow_clicked)
            clickedButtons.add(button)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}