package com.kindsundev.expense.manager.ui.home.menu.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.databinding.FragmentProfileBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.startLoadingDialog

class ProfileFragment : Fragment(), ProfileContact.View {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val args : ProfileFragmentArgs by navArgs()

    private lateinit var profilePresenter: ProfilePresenter
    private var profileFragmentManager: FragmentManager? = null
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var uri: Uri? = null
    private var bitmap: Bitmap? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        profileFragmentManager = activity?.supportFragmentManager
        registerActivityResult()
        registerReadExternalPermissions()

    }

    private fun registerActivityResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent == null || intent.data == null) {
                    Logger.error("Loading image from gallery failed")
                } else {
                    uri = intent.data
                    uri?.let { initBitmap(it) }
                    binding!!.imgUser.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun initBitmap(uri: Uri) {
        bitmap = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(
                requireContext().contentResolver, uri
            )
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun registerReadExternalPermissions() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted  ->
            if (isGranted) {
                openPhotoGallery()
            } else {
                val message = "This feature required a permission that the user has denied"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPhotoGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        profilePresenter = ProfilePresenter(this)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        val userDetail = args.user
        binding!!.edtName.hint = userDetail.name
        binding!!.edtEmail.hint = userDetail.email
        binding!!.edtPhoneNumber.hint = userDetail.phoneNumber
        Glide.with(binding!!.imgUser)
            .load(userDetail.photoUrl)
            .placeholder(R.drawable.img_user_default)
            .error(R.drawable.img_user_default)
            .centerCrop()
            .into(binding!!.imgUser)
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { onClickBack()}
        binding!!.btnChangePassword.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.imgUser.setOnClickListener { onClickUpdateImage() }
        binding!!.btnUpdate.setOnClickListener { onClickUpdateProfile() }
    }

    private fun onClickBack() {
        binding!!.btnBack.findNavController().popBackStack(R.id.menuFragment, false)
    }

    private fun onClickUpdateImage() {
        val permissions = Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(permissions)
    }

    private fun onClickUpdateProfile() {
        val name = binding!!.edtName.text.toString().trim()
        profilePresenter.updateProfile(uri.toString(), name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        profilePresenter.cleanUp()
    }

    override fun onLoad() {
        profileFragmentManager?.let { startLoadingDialog(loadingDialog, it, true) }
    }

    override fun onError(message: String) {
        profileFragmentManager?.let { startLoadingDialog(loadingDialog, it, false) }
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        profileFragmentManager?.let { startLoadingDialog(loadingDialog, it, false) }
        findNavController().popBackStack()
    }
}