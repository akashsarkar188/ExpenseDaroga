package akashsarkar188.expensedaroga.screens.home

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.FragmentHomeBinding
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.utils.NotificationHelper
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentFullMonthYearString
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        initObservers()
        initHistory()
        viewModel.fetchTransactionsForThisMonthYear()
        viewModel.fetchAllTransactions()

        return binding?.root
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
    }

    private fun initHeaderView(transactionData: ArrayList<TransactionDataModel>) {
        binding?.apply {
            introMessageTextView.text =
                "You have made ${transactionData.size} transactions so far \uD83E\uDD14"
            currentMonthTextView.text = getCurrentFullMonthYearString()
            monthCreditAmount.text = "₹${viewModel.getTotalCreditAmount()}"
            monthDebitAmount.text = "₹${viewModel.getTotalDebitAmount()}"
            monthLoanGivenAmount.text = "₹${viewModel.getTotalLoanGivenAmount()}"
            monthLoanTakenAmount.text = "₹${viewModel.getTotalLoanTakenAmount()}"
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