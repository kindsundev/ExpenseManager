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
import com.kindsundev.expense.manager.databinding.DialogUpdateEmailBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfileContact
import com.kindsundev.expense.manager.ui.home.menu.profile.ProfilePresenter
import com.kindsundev.expense.manager.utils.checkEmail
import com.kindsundev.expense.manager.utils.hideKeyboard
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class UpdateEmailDialog(
    private val callback: ResultUpdateCallBack
) : DialogFragment(), ProfileContact.View {
    private var _binding: DialogUpdateEmailBinding? = null
    private val binding get() = _binding

    private lateinit var profilePresenter: ProfilePresenter
    private val loadingDialog by lazy { LoadingDialog() }

    override fun getCurrentContext(): Context = requireContext()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        profilePresenter = ProfilePresenter(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdateEmailBinding.inflate(layoutInflater)

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
        binding!!.btnUpdate.setOnClickListener { handlerUpdateEmail() }
    }

    private fun handlerUpdateEmail() {
        val email = binding!!.edtEmail.text.toString().trim()
        if (checkValidEmail(email)) {
            profilePresenter.updateEmail(email)
        }
    }

    private fun checkValidEmail(email: String): Boolean {
        return when (checkEmail(email)) {
            Status.WRONG_EMAIL_EMPTY -> {
                activity?.showMessage(getCurrentContext().getString(R.string.email_not_null))
                false
            }
            Status.WRONG_EMAIL_PATTERN -> {
                activity?.showMessage(getCurrentContext().getString(R.string.email_invalidate))
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
        requestUpdateUI()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        closeDialog()
    }

    private fun requestUpdateUI() {
        val email = profilePresenter.getUserAfterUpdate().email
        if (email != null) {
            callback.updateEmail(email)
        }
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