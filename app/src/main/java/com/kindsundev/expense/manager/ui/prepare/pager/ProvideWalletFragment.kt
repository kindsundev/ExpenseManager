package com.kindsundev.expense.manager.ui.prepare.pager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentProvideWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletContract
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletPresenter
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class ProvideWalletFragment : Fragment(),
    PrepareWalletContract.View,  PrepareWalletContract.Result {
    private var _binding: FragmentProvideWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var mPrepareWalletPresenter: PrepareWalletPresenter
    private lateinit var mWalletAdapter: ProvideWalletAdapter
    private lateinit var mWallets : ArrayList<WalletModel>

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPrepareWalletPresenter = PrepareWalletPresenter(this)
        mWallets = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProvideWalletBinding.inflate(inflater, container, false)
        binding!!.rcvWallets.layoutManager = LinearLayoutManager(getCurrentContext())
        mPrepareWalletPresenter.handleGetWallets()
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        if (mWallets.isNotEmpty()) {
            mWallets.clear()
            mPrepareWalletPresenter.handleGetWallets()
        }
    }

    override fun onSuccessWallets(wallets: ArrayList<WalletModel>) {
        mWallets = wallets
        initDataToRecyclerView(wallets)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {}

    private fun initDataToRecyclerView(wallets: ArrayList<WalletModel>) {
        mWalletAdapter = ProvideWalletAdapter(wallets, object : PrepareWalletContract.Listener {
            override fun onClickWalletItem(walletModel: WalletModel) {
                startHomeActivity(walletModel.id.toString())
            }
        })
        binding!!.rcvWallets.adapter = mWalletAdapter
        switchLayout(wallets)
    }

    private fun startHomeActivity(id: String) {
        startActivity(
            Intent(requireActivity(), HomeActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putString(Constant.KEY_CURRENT_WALLET_ID, id)
                    }
                )
            })
    }

    private fun switchLayout(data: ArrayList<WalletModel>) {
        if (data.isEmpty()) {
            binding!!.rcvWallets.visibility = View.GONE
            binding!!.tvMessageNull.visibility = View.VISIBLE
        } else {
            binding!!.tvMessageNull.visibility = View.GONE
            binding!!.rcvWallets.visibility = View.VISIBLE
        }
    }

    override fun onResultCreateWallet(status: Boolean) {
        if (status) {
            mPrepareWalletPresenter.handleGetWallets()
            mWallets.clear()
            initDataToRecyclerView(mWallets)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mPrepareWalletPresenter.cleanUp()
    }
}