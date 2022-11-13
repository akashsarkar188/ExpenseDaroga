package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.ActivityAddTransactionBinding
import akashsarkar188.expensedaroga.utils.BUNDLE_MONTH_YEAR_STRING
import akashsarkar188.expensedaroga.utils.ObjectFactory
import akashsarkar188.expensedaroga.utils.getCurrentMonthYearString
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class AddTransactionActivity : AppCompatActivity() {

    private var binding: ActivityAddTransactionBinding? = null
    private val viewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction)

        init()
        initObservers()
    }

    private fun initObservers() {
        viewModel.allTransactions?.observe(this) {
            it?.let {
                if (it.success is List<*>) {
                    Log.e("XXX", "initObservers: " + it.success )
                }
            }
        }
    }

    private fun init() {
        // no month received? lets just focus on present!
        (intent.getStringExtra(BUNDLE_MONTH_YEAR_STRING)).let {
            if (it == null) {
                viewModel.selectMonthYear.value = getCurrentMonthYearString()
            } else {
                viewModel.selectMonthYear.value = it
            }
        }

        binding?.apply {
            // lets show all the transactions for the selected month
            transactionsRecyclerView.layoutManager =
                LinearLayoutManager(this@AddTransactionActivity)
        }
    }
}