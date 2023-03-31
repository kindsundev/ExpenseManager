package com.kindsundev.expense.manager.ui.prepare.pager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentProvideWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.dialog.ResultWalletCallback
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletContract
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletPresenter
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class ProvideWalletFragment : Fragment(),
    PrepareWalletContract.View, ResultWalletCallback.Create, PrepareWalletContract.Listener {
    private var _binding: FragmentProvideWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var prepareWalletPresenter: PrepareWalletPresenter
    private lateinit var walletAdapter: ProvideWalletAdapter
    private lateinit var wallets : ArrayList<WalletModel>
    private lateinit var bill : ArrayList<BillModel>
    private var currentClickedWalletId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareWalletPresenter = PrepareWalletPresenter(this)
        wallets = ArrayList()
        bill = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProvideWalletBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rcvWallets.layoutManager = LinearLayoutManager(context)
        prepareWalletPresenter.handlerGetWallets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        prepareWalletPresenter.cleanUp()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess(status: Boolean) {
        if (status) {
            startLoadingDialog(loadingDialog, parentFragmentManager, false)
            bill = prepareWalletPresenter.getBills()
            startHomeActivity()
        }
    }

    private fun startHomeActivity() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java).apply {
            putExtras(
                Bundle().apply {
                    putInt(Constant.KEY_CURRENT_WALLET, currentClickedWalletId)
                    putParcelableArrayList(Constant.KEY_WALLET, wallets)
                    putParcelableArrayList(Constant.KEY_BILL, bill)
                })
        })
    }

    override fun onSuccess() {
        initDataToRecyclerView()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initDataToRecyclerView() {
        wallets = prepareWalletPresenter.getWallets()
        walletAdapter = ProvideWalletAdapter(wallets, this)
        binding!!.rcvWallets.adapter = walletAdapter
        if (wallets.isEmpty()) {
            binding!!.rcvWallets.visibility = View.GONE
            binding!!.tvMessageNull.visibility = View.VISIBLE
        } else {
            binding!!.tvMessageNull.visibility = View.GONE
            binding!!.rcvWallets.visibility = View.VISIBLE
        }
    }

    override fun onClickWalletItem(walletModel: WalletModel) {
        prepareWalletPresenter.handlerGetTransactions(walletModel.id!!)
        currentClickedWalletId = walletModel.id
    }

    override fun resultCreateWallet(status: Boolean) {
        if (status) {
            prepareWalletPresenter.handlerGetWallets()
            wallets.clear()
            initDataToRecyclerView()
        }
    }
}