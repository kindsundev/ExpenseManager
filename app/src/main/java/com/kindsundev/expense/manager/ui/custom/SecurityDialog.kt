package com.kindsundev.expense.manager.ui.custom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.DialogSecurityBinding
import com.kindsundev.expense.manager.utils.startSignInActivity

class SecurityDialog(val message: String) : DialogFragment() {
    private var _binding: DialogSecurityBinding? = null
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogSecurityBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(
            requireActivity(), R.style.Theme_ExpenseManager
        ).apply {
            setCancelable(false)
            setView(binding!!.root)
        }.create()

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notification = "Please login before changing your $message"
        binding!!.tvMessage.text = notification
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnNo.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnYes.setOnClickListener { onClickYes() }
    }

    private fun onClickYes() {
        dialog!!.dismiss()
        activity?.startSignInActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}