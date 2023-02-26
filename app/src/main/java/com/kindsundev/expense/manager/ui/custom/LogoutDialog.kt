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
import com.kindsundev.expense.manager.databinding.DialogWarningBinding

class WarningDialog(
    private val message: String
) : DialogFragment() {
    private var _binding: DialogWarningBinding? = null
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogWarningBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(
            requireActivity(), R.style.Theme_ExpenseManager).apply {
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
        binding!!.tvMessage.text = message
        initListener()
        return binding!!.root
    }

    /*
    * WHY I JUS STOP EVERY DIALOG
    * This is a generic class, so will disable the currently displayed dialog.
    * Some of the behind-the-scenes logic will depend on the class calling it and handling it
    * */
    private fun initListener() {
        binding!!.btnNo.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnYes.setOnClickListener { dialog!!.dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}