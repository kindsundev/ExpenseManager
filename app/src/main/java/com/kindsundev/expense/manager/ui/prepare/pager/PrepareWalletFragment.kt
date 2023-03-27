package com.kindsundev.expense.manager.ui.prepare.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.databinding.FragmentPrepareWalletBinding
import com.kindsundev.expense.manager.utils.startHomeActivity

class PrepareWalletFragment : Fragment() {

    private var _binding: FragmentPrepareWalletBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrepareWalletBinding.inflate(inflater, container, false)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnOk.setOnClickListener {
            activity?.startHomeActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}