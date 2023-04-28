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
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.databinding.FragmentProfileBinding

import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.menu.profile.update.UpdateEmailDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.update.UpdateNameDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.update.UpdatePasswordDialog
import com.kindsundev.expense.manager.ui.home.menu.profile.update.ResultUpdateCallBack
import com.kindsundev.expense.manager.utils.toggleBottomNavigation
import com.kindsundev.expense.manager.utils.loadUserAvatar
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.startLoadingDialog

class ProfileFragment : Fragment(), ProfileContact.View, ResultUpdateCallBack {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val args : ProfileFragmentArgs by navArgs()
    private var user: UserModel? = null

    private lateinit var profilePresenter: ProfilePresenter
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var updateNameDialog: UpdateNameDialog
    private lateinit var updateEmailDialog: UpdateEmailDialog
    private lateinit var updatePasswordDialog: UpdatePasswordDialog

    override fun getCurrentContext(): Context = requireContext()

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
                    val uri = intent.data
                    uri?.let {
                        profilePresenter.updateAvatar(uri.toString())
                        loadNewImageForUser(it)
                    }
                }
            }
        }
    }

    private fun loadNewImageForUser(uri: Uri) {
        val bitmap = getBitmapFromUri(uri)
        if (bitmap != null) {
            binding!!.ivUserAvatar.setImageBitmap(bitmap)
        } else {
            binding!!.ivUserAvatar.setImageResource(R.drawable.img_user_default)
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
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
                val message = getCurrentContext().getString(R.string.permission_denied)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPhotoGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        activityResultLauncher.launch(
            Intent.createChooser(
                intent,
                getCurrentContext().getString(R.string.select_picture)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profilePresenter = ProfilePresenter(this)
        user = args.user
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        user?.let {
            binding!!.tvUserName.text = user?.name
            binding!!.tvUserEmail.text = user?.email
            activity?.loadUserAvatar(user?.photoUrl,
                R.drawable.img_user_default, binding!!.ivUserAvatar)
        }
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { onClickBack() }
        binding!!.ivUserAvatar.setOnClickListener { onClickUpdateAvatar() }
        binding!!.rlEditName.setOnClickListener { onClickUpdateName() }
        binding!!.rlEditEmail.setOnClickListener { onCLickUpdateEmail() }
        binding!!.rlEditPassword.setOnClickListener { onClickUpdatePassword() }
        binding!!.btnUpdatePremium.setOnClickListener { activity?.onFeatureIsDevelop() }
        binding!!.btnPremiumSecurity.setOnClickListener { activity?.onFeatureIsDevelop() }
    }

    private fun onClickBack() {
        binding!!.btnBack.findNavController().popBackStack(R.id.menuFragment, false)
    }

    private fun onClickUpdateAvatar() {
        val permissions = Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(permissions)
    }

    private fun onClickUpdateName() {
        updateNameDialog = UpdateNameDialog(this)
        updateNameDialog.show(parentFragmentManager, Constant.UPDATE_NAME_DIALOG_NAME)
    }

    override fun updateName(name: String) {
        binding!!.tvUserName.text = name
    }

    private fun onCLickUpdateEmail() {
        updateEmailDialog = UpdateEmailDialog(this)
        updateEmailDialog.show(parentFragmentManager, Constant.UPDATE_EMAIL_DIALOG_NAME)
    }

    override fun updateEmail(email: String) {
        binding!!.tvUserEmail.text = email
    }

    private fun onClickUpdatePassword() {
        updatePasswordDialog = UpdatePasswordDialog()
        updatePasswordDialog.show(parentFragmentManager, Constant.UPDATE_PASSWORD_DIALOG_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleBottomNavigation(requireActivity() as HomeActivity, true)
        _binding = null
        profilePresenter.cleanUp()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        findNavController().popBackStack()
    }

    override fun onSuccess(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}