package com.kindsundev.expense.manager.ui.home.menu.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentSettingsBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.requestPremium
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { it.findNavController().popBackStack() }
        binding!!.rlTheme.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.rlLanguage.setOnClickListener { openSettingsLanguage() }
        binding!!.rlDateFormat.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.rlFirstDayOfWeek.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.rlFirstDayOfMonth.setOnClickListener {activity?.onFeatureIsDevelop() }
        binding!!.rlFirstMonthOfYear.setOnClickListener { activity?.onFeatureIsDevelop()}
        binding!!.rlNotificationTone.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.rlDailyReminder.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.rlSecurity.setOnClickListener { activity?.requestPremium()}
        binding!!.rlImportExport.setOnClickListener { activity?.requestPremium()}
        binding!!.rlBackupRestore.setOnClickListener { activity?.requestPremium() }
    }

    private fun openSettingsLanguage() {
        val dialog = LanguageDialog()
        dialog.show(parentFragmentManager, Constant.UPDATE_LANGUAGE_DIALOG_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
    }
}