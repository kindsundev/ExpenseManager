package com.kindsundev.expense.manager.ui.home.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.firebase.AuthFirebase
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.databinding.FragmentMenuBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.LogoutDialog
import com.kindsundev.expense.manager.utils.startLoadingDialog

class MenuFragment : Fragment(), MenuContract.View {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }
    private lateinit var menuPresenter: MenuPresenter
    private lateinit var auth: AuthFirebase
    private var user: UserModel? = null
    private var menuFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuPresenter = MenuPresenter(this)
        auth = AuthFirebase()
        menuFragmentManager = activity?.supportFragmentManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        user = menuPresenter.getCurrentUser()
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        user?.let {
            var name: String? = it.name.toString()
            val email = it.email.toString()
            val photoUrl = it.photoUrl
            if (name.isNullOrEmpty()) {
                name = "Edit Here"
            }
            binding!!.tvUserName.text = name
            binding!!.tvUserEmail.text = email
            Glide.with(binding!!.imgUserAvatar)
                .load(photoUrl)
                .placeholder(R.drawable.img_user_default)
                .error(R.drawable.img_user_default)
                .centerCrop()
                .into(binding!!.imgUserAvatar)
        }
    }

    private fun initListener() {
        binding!!.btnLogout.setOnClickListener { onCLickLogout() }
        binding!!.imgUserAvatar.setOnClickListener { onStartUserInfo() }
    }

    private fun onCLickLogout() {
        menuFragmentManager?.let {
            val message = "Do you want really logout?"
            menuFragmentManager?.let {
                LogoutDialog(message).show(it, Constant.LOGOUT_DIALOG_NAME)
            }
        }
    }

    private fun onStartUserInfo() {
        val action = user?.let { MenuFragmentDirections.actionMenuFragmentToProfileFragment(it) }
        if (action != null) {
            binding!!.imgUserAvatar.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}