package com.kindsundev.expense.manager.ui.home.menu.profile.update

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.databinding.DialogUpdateNameBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfileContact
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfilePresenter
import com.kindsundev.expense.manager.utils.hideKeyboard
import com.kindsundev.expense.manager.utils.startLoadingDialog

class UpdateNameDialog : DialogFragment(), ProfileContact.View {
    private var _binding: DialogUpdateNameBinding? = null
    private val binding get() = _binding

    private lateinit var profilePresenter: ProfilePresenter
    private val loadingDialog by lazy { LoadingDialog() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        profilePresenter = ProfilePresenter(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdateNameBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(
            requireActivity(), R.style.Theme_ExpenseManager).apply {
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
        binding!!.btnBack.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnUpdate.setOnClickListener { handlerUpdateName() }
    }

    private fun handlerUpdateName() {
        val name = binding!!.edtName.text.toString().trim()
        profilePresenter.updateName(name)
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        closeDialog()
    }

    override fun onSuccess() {}

    override fun onLoad() {
        hideKeyboard()
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}