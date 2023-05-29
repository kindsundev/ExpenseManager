package com.kindsundev.expense.manager.ui.home.bag.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.databinding.FragmentNotificationBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding
    private val args by navArgs<NotificationFragmentArgs>()

    private var mPlans: ArrayList<PlanModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        getDataFromBagFragment()
        initRecyclerView()
        initListener()
        return binding!!.root
    }

    private fun getDataFromBagFragment() {
        val planDoneString = args.plans
        mPlans = Gson().fromJson(planDoneString, object : TypeToken<ArrayList<PlanModel>>() {}.type)
    }

    private fun initRecyclerView() {
        if (mPlans != null) {
            binding!!.tvMessage.visibility = View.GONE
            binding!!.rcvPlans.apply {
                layoutManager = LinearLayoutManager(requireContext())
                hasFixedSize()
                adapter = NotificationAdapter(mPlans!!, object : NotificationContract.Listener {
                    override fun onClickNotificationItem(message: String) {
                        activity?.showMessage(message)
                    }
                })
            }
        } else {
            binding!!.tvMessage.visibility = View.VISIBLE
            binding!!.tvMessage.text = requireContext().getString(R.string.notification_is_null)
        }
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