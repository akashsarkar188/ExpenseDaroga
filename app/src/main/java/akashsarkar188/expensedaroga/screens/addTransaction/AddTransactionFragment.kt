package akashsarkar188.expensedaroga.screens.addTransaction

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.FragmentAddTransactionBinding
import akashsarkar188.expensedaroga.screens.addTransaction.adapter.TransactionAdapter
import akashsarkar188.expensedaroga.screens.addTransaction.adapter.TransactionTypeAdapter
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionCategories
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.utils.BUNDLE_MONTH_YEAR_STRING
import akashsarkar188.expensedaroga.utils.ObjectFactory
import akashsarkar188.expensedaroga.utils.commonMethods.*
import akashsarkar188.expensedaroga.utils.popup.transaction.ActionType
import android.animation.LayoutTransition
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*

class AddTransactionFragment : Fragment() {

    private var binding: FragmentAddTransactionBinding? = null
    private val viewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionTypeAdapter: TransactionTypeAdapter

    companion object {
        @JvmStatic
        fun newInstance(monthYearString: String?) = AddTransactionFragment().apply {
            arguments = Bundle().apply {
                if (monthYearString != null) {
                    putString(BUNDLE_MONTH_YEAR_STRING, monthYearString)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(BUNDLE_MONTH_YEAR_STRING)) {
                viewModel.selectedMonthYear.value = it.getString(BUNDLE_MONTH_YEAR_STRING)
                if (it.getString(BUNDLE_MONTH_YEAR_STRING) == getCurrentMonthYearString()) {
                    // current month is selected, so lets get the current date
                    viewModel.selectedDate.value = getCurrentDateObject()
                } else {
                    viewModel.selectedDate.value =
                        getLastDateForMonthYear(it.getString(BUNDLE_MONTH_YEAR_STRING)!!)
                }
            } else {
                viewModel.selectedMonthYear.value = getCurrentMonthYearString()
                viewModel.selectedDate.value = getCurrentDateObject()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_transaction, container, false)

        init()
        onClickListeners()
        initObservers()
        viewModel.fetchTransactionsForThisMonthYear()

        return binding?.root
    }

    private fun init() {

        binding?.apply {
            transactionsRecyclerView.layoutManager =
                LinearLayoutManager(context)

            transactionTypeRecyclerView.layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            transactionAdapter = TransactionAdapter() { action, dataObject ->
                when (action) {
                    ActionType.DELETE -> {
                        viewModel.deleteThisTransactions(dataObject)
                    }
                    ActionType.REDO -> {

                    }
                    ActionType.EDIT -> {

                    }
                }
                ObjectFactory.globalRefreshMutableLiveData.value = true
            }
            transactionTypeAdapter = TransactionTypeAdapter()

            transactionsRecyclerView.adapter = transactionAdapter
            transactionTypeRecyclerView.adapter = transactionTypeAdapter

            monthYearTextView.text =
                getFullMonthYearStringFromMonthYear(viewModel.selectedMonthYear.value!!)
        }

        transactionTypeAdapter.addData(viewModel.getTransactionTypes())
    }

    private fun onClickListeners() {
        binding?.apply {
            saveTransactionImageView.setOnClickListener {
                if (validateData()) {
                    viewModel.addTransaction(prepareModelToSave())
                    clearInputsAndCloseKeyboard()
                    ObjectFactory.globalRefreshMutableLiveData.value = true
                }
            }

            transactionDateLinearLayout.setOnClickListener {
                context?.let { context ->
                    DatePickerDialog(
                        context,
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

            selectMonthCardView.setOnClickListener {
                context?.let { context ->
                    DatePickerDialog(
                        context,
                        { view, year, month, day ->
                            val cal = Calendar.getInstance()
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, month)
                            cal.set(Calendar.DAY_OF_MONTH, day)

                            viewModel.selectedMonthYear.value = getMonthYearStringFromDate(cal.time)

                            if (viewModel.selectedMonthYear.value == getCurrentMonthYearString()) {
                                // current month is selected, so lets get the current date
                                viewModel.selectedDate.value = getCurrentDateObject()
                            } else {
                                viewModel.selectedDate.value =
                                    getLastDateForMonthYear(viewModel.selectedMonthYear.value!!)
                            }
                            viewModel.fetchTransactionsForThisMonthYear()
                        },
                        getYearIntFromMonthYearStr(viewModel.selectedMonthYear.value),
                        getMonthIntFromMonthYearStr(viewModel.selectedMonthYear.value),
                        1
                    ).show()
                }
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
        viewModel.allTransactions?.observe(viewLifecycleOwner) {
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

        viewModel.selectedDate.observe(viewLifecycleOwner) {
            it?.let {
                binding?.apply {
                    transactionDateTextView.text = "On ${getDateInDD_MMM(it)}"
                }
            }
        }

        viewModel.selectedMonthYear.observe(viewLifecycleOwner) {
            binding?.monthYearTextView?.text =
                getFullMonthYearStringFromMonthYear(viewModel.selectedMonthYear.value!!)
        }

        ObjectFactory.globalRefreshMutableLiveData.observe(viewLifecycleOwner) {
            doIfTrue(it) {
                viewModel.fetchTransactionsForThisMonthYear()
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
                        params.bottomMargin = 10.toPx.toInt()
                        transactionMetaInputLinearLayout.visibility = View.GONE
                        closeKeyboard(context)
                    } else {
                        amountCardView.radius = 0.toPx
                        params.marginStart = 0
                        params.marginEnd = 0
                        params.bottomMargin = 0.toPx.toInt()
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
                it.transactionAmountEditText.openKeyboard()
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
            closeKeyboard(context)
        }
    }
}