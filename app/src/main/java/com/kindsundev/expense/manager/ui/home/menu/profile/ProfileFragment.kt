package com.kindsundev.expense.manager.ui.home.menu.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kindsundev.expense.manager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val args : ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        val userDetail = args.user
        binding!!.edtName.hint = userDetail.name
        binding!!.edtEmail.hint = userDetail.email
        binding!!.edtPhoneNumber.hint = userDetail.phoneNumber
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {  }
        binding!!.btnChangePassword.setOnClickListener {  }

        binding!!.imgUser.setOnClickListener {

        }
        binding!!.btnUpdate.setOnClickListener {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}