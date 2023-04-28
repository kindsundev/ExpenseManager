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
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.databinding.DialogUpdatePasswordBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfileContact
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfilePresenter
import com.kindsundev.expense.manager.utils.checkPassword
import com.kindsundev.expense.manager.utils.hideKeyboard
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class UpdatePasswordDialog : DialogFragment(), ProfileContact.View {
    private var _binding: DialogUpdatePasswordBinding? = null
    private val binding get() = _binding

    private lateinit var profilePresenter: ProfilePresenter
    private val loadingDialog by lazy { LoadingDialog() }

    override fun getCurrentContext(): Context = requireContext()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        profilePresenter = ProfilePresenter(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdatePasswordBinding.inflate(layoutInflater)

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
        binding!!.btnBack.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnUpdate.setOnClickListener { handlerUpdatePassword() }
    }

    private fun handlerUpdatePassword() {
        val password = binding!!.edtPassword.text.toString().trim()
        if (checkValidPassword(password)) {
            profilePresenter.updatePassword(password)
        }
    }

    private fun checkValidPassword(password: String): Boolean {
        return when (checkPassword(password)) {
            Status.WRONG_PASSWORD_EMPTY -> {
                activity?.showMessage(getCurrentContext().getString(R.string.password_not_null))
                false
            }
            Status.WRONG_PASSWORD_LENGTH -> {
                activity?.showMessage(getCurrentContext().getString(R.string.password_to_short))
                false
            }
            else -> true
        }
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