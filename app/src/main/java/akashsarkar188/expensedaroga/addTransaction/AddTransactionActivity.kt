package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.addTransaction.adapter.TransactionAdapter
import akashsarkar188.expensedaroga.addTransaction.adapter.TransactionTypeAdapter
import akashsarkar188.expensedaroga.addTransaction.model.TransactionCategories
import akashsarkar188.expensedaroga.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.databinding.ActivityAddTransactionBinding
import akashsarkar188.expensedaroga.utils.BUNDLE_MONTH_YEAR_STRING
import akashsarkar188.expensedaroga.utils.commonMethods.*
import akashsarkar188.expensedaroga.utils.popup.transaction.ActionType
import android.animation.LayoutTransition
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*
import kotlin.collections.ArrayList

class AddTransactionActivity : AppCompatActivity() {

    private var binding: ActivityAddTransactionBinding? = null
    private val viewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionTypeAdapter: TransactionTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction)

        init()
        onClickListeners()
        initObservers()
        viewModel.fetchTransactionsForThisMonthYear()
    }

    private fun init() {
        // no month received? lets just focus on present!
        (intent.getStringExtra(BUNDLE_MONTH_YEAR_STRING)).let {
            if (it == null) {
                viewModel.selectedMonthYear.value = getCurrentMonthYearString()
                viewModel.selectedDate.value = getCurrentDateObject()
            } else {
                viewModel.selectedMonthYear.value = it

                if (it == getCurrentMonthYearString()) {
                    // current month is selected, so lets get the current date
                    viewModel.selectedDate.value = getCurrentDateObject()
                } else {
                    viewModel.selectedDate.value = getLastDateForMonthYear(it)
                }
            }
        }

        binding?.apply {
            transactionsRecyclerView.layoutManager =
                LinearLayoutManager(this@AddTransactionActivity)

            transactionTypeRecyclerView.layoutManager =
                LinearLayoutManager(
                    this@AddTransactionActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            transactionAdapter = TransactionAdapter() {
                action, dataObject ->
                when(action) {
                    ActionType.DELETE -> {
                        viewModel.deleteThisTransactions(dataObject)
                    }
                    ActionType.REDO -> {

                    }
                    ActionType.EDIT -> {

                    }
                }
            }
            transactionTypeAdapter = TransactionTypeAdapter()

            transactionsRecyclerView.adapter = transactionAdapter
            transactionTypeRecyclerView.adapter = transactionTypeAdapter

            monthYearTextView.text = viewModel.selectedMonthYear.value
        }

        transactionTypeAdapter.addData(viewModel.getTransactionTypes())
    }

    private fun onClickListeners() {
        binding?.apply {
            saveTransactionImageView.setOnClickListener {
                if (validateData()) {
                    viewModel.addTransaction(prepareModelToSave())
                    clearInputsAndCloseKeyboard()
                }
            }

            transactionDateLinearLayout.setOnClickListener {

                DatePickerDialog(
                    this@AddTransactionActivity,
                    { view, year, month, day ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, day)

                        viewModel.selectedDate.value = cal.time
                    },
                    getYearIntFromMonthYearStr(viewModel.selectedMonthYear.value),
                    getMonthIntFromMonthYearStr(viewModel.selectedMonthYear.value),
                    1
                ).show()
            }
        }
    }

    private fun prepareModelToSave(): TransactionDataModel {
        val payload = TransactionDataModel()

        binding?.let {
            payload.amount = it.transactionAmountEditText.toDouble()
            payload.date = viewModel.selectedDate.value?.time
            payload.meta = TransactionDataModel.TransactionMeta(
                TransactionCategories.daily_expense.toString(),
                transactionTypeAdapter.getSelectedItem(),
                it.transactionNoteEditText.text?.toString()
            )
        }
        return payload
    }

    private fun initObservers() {
        viewModel.allTransactions?.observe(this) {
            it?.let {
                if (it.success is List<*>) {
                    Log.e("XXX", "initObservers: " + it.success)
                    // success
                    transactionAdapter.addData(it.success as ArrayList<TransactionDataModel>)
                    binding?.transactionsRecyclerView?.smoothScrollToPosition(transactionAdapter.itemCount - 1)
                } else {
                    // error
                }
            }
        }

        viewModel.selectedDate.observe(this) {
            it?.let {
                binding?.apply {
                    transactionDateTextView.text = "On ${getDateInDD_MMM(it)}"
                }
            }
        }

        binding?.apply {
            transactionAmountEditText.addTextChangedListener {
                it?.let {
                    val params = amountCardView.layoutParams as LinearLayout.LayoutParams
                    (transactionMetaInputLinearLayout as ViewGroup).layoutTransition.enableTransitionType(
                        LayoutTransition.CHANGING
                    )
                    if (it.isNullOrEmpty()) {
                        amountCardView.radius = 30.toPx
                        params.marginStart = 16.toPx.toInt()
                        params.marginEnd = 16.toPx.toInt()
                        transactionMetaInputLinearLayout.visibility = View.GONE
                        closeKeyboard(this@AddTransactionActivity)
                    } else {
                        amountCardView.radius = 0.toPx
                        params.marginStart = 0
                        params.marginEnd = 0
                        transactionMetaInputLinearLayout.visibility = View.VISIBLE
                    }
                    (amountCardView as ViewGroup).layoutTransition.enableTransitionType(
                        LayoutTransition.CHANGING
                    )
                    amountCardView.layoutParams = params
                }
            }
        }
    }

    private fun validateData(): Boolean {
        binding?.let {
            if (it.transactionAmountEditText.isNullOrEmpty()) {
                it.transactionAmountEditText.shakeView()
                return false
            }
            return true
        }
        return false
    }

    private fun clearInputsAndCloseKeyboard() {
        binding?.apply {
            transactionAmountEditText.setText("")
            transactionNoteEditText.setText("")
            closeKeyboard(this@AddTransactionActivity)
        }
    }
}