package akashsarkar188.expensedaroga.screens.addTransaction.repository

import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.services.AppDatabase
import akashsarkar188.expensedaroga.utils.ObjectFactory
import akashsarkar188.expensedaroga.utils.ResultClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TransactionRepository {

    private val appDatabase: AppDatabase by lazy {
        ObjectFactory.appDatabaseInstance
    }

    suspend fun getAllTransactions(): ResultClass {
        return withContext(Dispatchers.IO) {
            val transactionDao = appDatabase.transactionDao()
            val transactionsList = transactionDao.getAllTransactions()

            ResultClass(null, transactionsList)
        }
    }

    suspend fun getTransactionBetweenTimestamps(
        fromTimeStamp: Long,
        toTimeStamp: Long
    ): ResultClass {
        return withContext(Dispatchers.IO) {
            val transactionDao = appDatabase.transactionDao()
            val transactionsList =
                transactionDao.getTransactionsBetweenTimestamp(
                    fromTimeStamp, // to consider the whole first day going back 24 hours
                    toTimeStamp
                )

            ResultClass(null, transactionsList)
        }
    }

    suspend fun addTransaction(transaction: TransactionDataModel) {
        withContext(Dispatchers.IO) {
            val transactionDao = appDatabase.transactionDao()
            transactionDao.addTransaction(transaction)
        }

    }

    suspend fun deleteAllTransactions() {
        withContext(Dispatchers.IO) {
            val transactionDao = appDatabase.transactionDao()
            transactionDao.deleteAllTransactions()
        }
    }

    suspend fun deleteThisTransactions(transactionId: Int) {
        withContext(Dispatchers.IO) {
            val transactionDao = appDatabase.transactionDao()
            transactionDao.deleteTransactionById(transactionId)
        }
    }
}