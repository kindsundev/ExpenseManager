package com.kindsundev.expense.manager.ui.home.menu.settings

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
import com.kindsundev.expense.manager.databinding.DialogLanguageBinding
import com.kindsundev.expense.manager.utils.showToast

class LanguageDialog : DialogFragment() {
    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLanguageBinding.inflate(layoutInflater)

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
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.rBtnEnglish.setOnClickListener {
            this.dismiss()
            showAlterDialogAppReset()
        }
        binding!!.rBtnVietnamese.setOnClickListener {
            showAlterDialogAppReset()
            this.dismiss()
        }
    }

    private fun showAlterDialogAppReset() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmAlertDialog)
            .setTitle(R.string.restart_app)
            .setMessage(R.string.message_for_restart_app)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                activity?.showToast("Success")
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}