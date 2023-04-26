package com.kindsundev.expense.manager.ui.home.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.databinding.FragmentMenuBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.LogoutDialog
import com.kindsundev.expense.manager.utils.loadUserAvatar
import com.kindsundev.expense.manager.utils.startLoadingDialog

class MenuFragment : Fragment(), MenuContract.View {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var menuPresenter: MenuPresenter
    private var menuFragmentManager: FragmentManager? = null
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuPresenter = MenuPresenter(this)
        menuFragmentManager = activity?.supportFragmentManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        user = menuPresenter.getCurrentUser()
        user?.let {
            var name: String? = it.name.toString()
            val email = it.email.toString()
            val photoUrl = it.photoUrl
            if (name.isNullOrEmpty()) name = "Edit Here"
            binding!!.tvUserName.text = name
            binding!!.tvUserEmail.text = email
            activity?.loadUserAvatar(photoUrl, R.drawable.img_user_default, binding!!.imgUserAvatar)
        }
    }

    private fun initListener() {
        binding!!.imgUserAvatar.setOnClickListener { onStartUserInfo() }
        binding!!.rlLogout.setOnClickListener { onCLickLogout() }
        binding!!.rlTutorial.setOnClickListener { }
        binding!!.rlFeedback.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToFeedbackFragment())
        }
        binding!!.rlContact.setOnClickListener { openContactEmail() }
        binding!!.rlPolicy.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToPolicyFragment())
        }
        binding!!.rlAbout.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAboutFragment())
        }
    }

    private fun onStartUserInfo() {
        val action = user?.let { MenuFragmentDirections.actionMenuFragmentToProfileFragment(it) }
        if (action != null) {
            binding!!.imgUserAvatar.findNavController().navigate(action)
        }
    }

    private fun onCLickLogout() {
        menuFragmentManager?.let {
            LogoutDialog().show(it, Constant.LOGOUT_DIALOG_NAME)
        }
    }

    private fun openContactEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.duongtiendev@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Expense Manager App")
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onLoad() {
        menuFragmentManager?.let { startLoadingDialog(loadingDialog, it, true) }
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        menuFragmentManager?.let { startLoadingDialog(loadingDialog, it, false) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}