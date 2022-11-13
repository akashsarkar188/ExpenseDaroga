package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.utils.ResultClass
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    var selectMonthYear : MutableLiveData<String?> = MutableLiveData()

    var allTransactions : MutableLiveData<ResultClass?>? = null
    get (){
        if (field == null) {
            field = MutableLiveData()
        }
        return field
    }

    fun fetchAllTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            allTransactions!!.postValue(TransactionRepository.getAllTransactions())
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            TransactionRepository.deleteAllTransactions()
            allTransactions!!.postValue(TransactionRepository.getAllTransactions())
        }
    }

    fun addTransaction(transaction : TransactionDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            TransactionRepository.addTransaction(transaction)
        }
    }
}