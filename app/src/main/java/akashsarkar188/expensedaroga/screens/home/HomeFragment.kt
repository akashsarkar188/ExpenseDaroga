package akashsarkar188.expensedaroga.screens.home

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.FragmentHomeBinding
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.services.SharedPreferenceHelper
import akashsarkar188.expensedaroga.utils.NotificationHelper
import akashsarkar188.expensedaroga.utils.ObjectFactory
import akashsarkar188.expensedaroga.utils.commonMethods.*
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    private var adapter: HistoryAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        init()
        onClickListeners()
        initObservers()
        initHistory()
        viewModel.fetchTransactionsForThisMonthYear()
        viewModel.fetchAllTransactions()

        return binding?.root
    }

    private fun init() {
        binding?.apply {

        }
    }

    private fun onClickListeners() {
        binding?.availableBalanceTextView?.setOnClickListener {
            if (binding?.availableBalanceInfoTextView?.visibility == View.VISIBLE) {
                binding?.availableBalanceInfoTextView?.visibility = View.GONE
            } else {
                binding?.availableBalanceInfoTextView?.visibility = View.VISIBLE
            }
        }

        binding?.debitsLinearLayout?.setOnClickListener {
            if (binding?.debitDetailsLayout?.visibility == View.VISIBLE) {
                binding?.debitDetailsLayout?.visibility = View.GONE
            } else {
                binding?.debitDetailsLayout?.visibility = View.VISIBLE
            }
        }
    }

    private fun initObservers() {
        viewModel.allTransactions?.observe(viewLifecycleOwner) {
            it?.let {
                if (it.success is List<*>) {
                    Log.e("XXX", "initObservers: " + it.success)
                    // success
                    val transactionData = it.success as ArrayList<TransactionDataModel>
                    viewModel.updateValues()
                    initHeaderView(transactionData)
                } else {
                    // error
                }
            }
        }

        viewModel.monthlyTransactions?.observe(viewLifecycleOwner) {
            adapter?.addData(it)
        }

        ObjectFactory.globalRefreshMutableLiveData.observe(viewLifecycleOwner) {
            doIfTrue(it) {
                viewModel.fetchTransactionsForThisMonthYear()
                viewModel.fetchAllTransactions()
            }
        }

        ObjectFactory.userNameChangeMutableLiveData.observe(viewLifecycleOwner) {
            doIfTrue(it) {
                binding?.greetingTextView?.text = "What's Up ${SharedPreferenceHelper.getUserName()}!"
            }
        }
    }

    private fun initHeaderView(transactionData: ArrayList<TransactionDataModel>) {
        binding?.apply {
            currentMonthTextView.text = "${getCurrentFullMonthYearString()} 🔍"
            historyTextView.text = "History 📊"
            monthCreditAmount.text = "₹${formatAsCurrency(viewModel.getTotalCreditAmount())}"
            monthDebitAmount.text = "₹${formatAsCurrency(viewModel.getTotalDebitAmount())}"
            cashDebitAmount.text = "₹${formatAsCurrency(viewModel.getTotalCashDebitAmount())}"
            creditCardDebitAmount.text = "₹${formatAsCurrency(viewModel.getTotalCreditCardDebitAmount())}"
            monthLoanGivenAmount.text = "₹${formatAsCurrency(viewModel.getTotalLoanGivenAmount())}"
            monthLoanTakenAmount.text = "₹${formatAsCurrency(viewModel.getTotalLoanTakenAmount())}"
            availableBalanceAmount.text = "₹${formatAsCurrency(viewModel.getBalanceAmount())}"

            if (viewModel.isCreditGreaterThanDebit()) {
                introMessageTextView.text =
                    "Everything is under control 🙌"
            } else {
                introMessageTextView.text =
                    "Expenses are getting out of hands 🫣"
            }

            context?.let { context ->
                doIfTrue (viewModel.isBalanceAmountPositive()) {
                    availableBalanceAmount.setTextColor(ContextCompat.getColor(context, R.color.themeGreen))
                }
                doIfFalseOrNull(viewModel.isBalanceAmountPositive()){
                    availableBalanceAmount.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                doIfFalse(viewModel.isBalanceAmountPositive()){
                    availableBalanceAmount.setTextColor(ContextCompat.getColor(context, R.color.red_300))
                }
            }

            greetingTextView.text = "What's Up ${SharedPreferenceHelper.getUserName()}!"
        }
    }

    private fun initHistory() {
        binding?.apply {
            monthsHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
            adapter = HistoryAdapter() {
                context?.let { context ->
                    NotificationHelper.openTransactionsInBubble(context, it.month!!)
                }
            }
            monthsHistoryRecyclerView.adapter = adapter
            initSwipeBehavior(monthsHistoryRecyclerView)
        }
    }

    private fun initSwipeBehavior(recyclerView: RecyclerView) {
        val recyclerViewSwipeHelper = RecyclerViewSwipeHelper(adapter!!)
        val itemTouchHelper = ItemTouchHelper(recyclerViewSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}