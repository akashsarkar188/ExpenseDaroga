package akashsarkar188.expensedaroga.screens.home

import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionType
import akashsarkar188.expensedaroga.screens.addTransaction.repository.TransactionRepository
import akashsarkar188.expensedaroga.utils.ResultClass
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import akashsarkar188.expensedaroga.utils.commonMethods.getFirstDateForMonthYear
import akashsarkar188.expensedaroga.utils.commonMethods.getLastDateForMonthYear
import akashsarkar188.expensedaroga.utils.commonMethods.getMonthYearStringFromDate
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel : ViewModel() {

    val currentMonth: String by lazy {
        getCurrentMonthYearString()
    }

    var allTransactions: MutableLiveData<ResultClass?>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }

    var monthlyTransactions: MutableLiveData<ArrayList<MonthDataModel>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }

    private var totalCreditedAmount = "0.0"
    private var totalDebitedAmount = "0.0"
    private var totalLoanTakenAmount = "0.0"
    private var totalLoanGivenAmount = "0.0"

    fun fetchTransactionsForThisMonthYear() {
        viewModelScope.launch(Dispatchers.IO) {
            val firstDate = getFirstDateForMonthYear(currentMonth)
            val lastDate = getLastDateForMonthYear(currentMonth)
            allTransactions!!.postValue(
                TransactionRepository.getTransactionBetweenTimestamps(
                    firstDate.time,
                    lastDate.time
                )
            )
        }
    }

    fun updateValues() {
        allTransactions?.value?.let {
            if (it.success is List<*>) {
                val transactionData = it.success as ArrayList<TransactionDataModel>
                var credit = 0.0
                var debit = 0.0
                var loanTaken = 0.0
                var loanGiven = 0.0

                for (data in transactionData) {
                    when (data.meta?.transactionType) {
                        TransactionType.CREDIT -> {
                            credit += data.amount ?: 0.0
                        }
                        TransactionType.DEBIT -> {
                            Log.e("XXX", "fetchTransactionsForThisMonthYear: " + data.amount)
                            debit += data.amount ?: 0.0
                        }
                        TransactionType.LOAN_GIVEN -> {
                            loanGiven += data.amount ?: 0.0
                        }
                        TransactionType.LOAN_TAKEN -> {
                            loanTaken += data.amount ?: 0.0
                        }
                        else -> {

                        }
                    }
                }

                totalCreditedAmount = credit.toString()
                totalDebitedAmount = debit.toString()
                totalLoanTakenAmount = loanTaken.toString()
                totalLoanGivenAmount = loanGiven.toString()
            }
        }
    }

    fun fetchAllTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = TransactionRepository.getAllTransactions()
            if (result.success is List<*>) {
                val transactionData = result.success as ArrayList<TransactionDataModel>

                val tempHashMap = HashMap<String, MonthDataModel>()

                for (data in transactionData) {
                    val date = Date()
                    date.time = data.date!!

                    val monthYear = getMonthYearStringFromDate(date)

                    // already contains the month so lets get the model and make changes
                    if (tempHashMap.containsKey(monthYear)) {
                        val monthData = tempHashMap[monthYear]
                        when (data.meta?.transactionType) {
                            TransactionType.CREDIT -> monthData!!.creditAmount += data.amount!!
                            TransactionType.DEBIT -> monthData!!.debitAmount += data.amount!!
                            TransactionType.LOAN_TAKEN -> monthData!!.loanTakenAmount += data.amount!!
                            TransactionType.LOAN_GIVEN -> monthData!!.loanGivenAmount += data.amount!!
                            null -> TODO()
                        }
                    }
                    // month is not available so lets init model and save the amount
                    else {
                        val monthData = MonthDataModel()
                        monthData.month = monthYear
                        when (data.meta?.transactionType) {
                            TransactionType.CREDIT -> monthData!!.creditAmount += data.amount!!
                            TransactionType.DEBIT -> monthData!!.debitAmount += data.amount!!
                            TransactionType.LOAN_TAKEN -> monthData!!.loanTakenAmount += data.amount!!
                            TransactionType.LOAN_GIVEN -> monthData!!.loanGivenAmount += data.amount!!
                            null -> TODO()
                        }
                        tempHashMap[monthYear] = monthData
                    }
                }

                val monthList = ArrayList<MonthDataModel>()

                tempHashMap.forEach {
                    monthList.add(it.value)
                }

                monthList.sortBy {
                    it.month
                }

                monthlyTransactions!!.postValue(monthList)
            }
        }
    }

    fun getTotalCreditAmount() = totalCreditedAmount

    fun getTotalDebitAmount() = totalDebitedAmount

    fun getTotalLoanGivenAmount() = totalLoanGivenAmount

    fun getTotalLoanTakenAmount() = totalLoanTakenAmount

    fun isCreditGreaterThanDebit(): Boolean {
        return totalCreditedAmount.toDouble() > totalDebitedAmount.toDouble()
    }

    fun getBalanceAmount(): String =
        ((totalCreditedAmount.toDouble() + totalLoanTakenAmount.toDouble())
                - (totalDebitedAmount.toDouble() + totalLoanGivenAmount.toDouble())).toString()

    fun isBalanceAmountPositive(): Boolean? {
        return if (((totalCreditedAmount.toDouble() + totalLoanTakenAmount.toDouble())
                - (totalDebitedAmount.toDouble() + totalLoanGivenAmount.toDouble())) == 0.0) {
            null
        } else {
            ((totalCreditedAmount.toDouble() + totalLoanTakenAmount.toDouble())
                    - (totalDebitedAmount.toDouble() + totalLoanGivenAmount.toDouble()) > 0)
        }
    }
}