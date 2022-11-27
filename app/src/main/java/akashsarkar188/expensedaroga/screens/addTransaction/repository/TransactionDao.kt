package akashsarkar188.expensedaroga.screens.addTransaction.repository

import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("select * from `$TABLE_TRANSACTION`")
    fun getAllTransactions() : List<TransactionDataModel>

    @Query("select * from `$TABLE_TRANSACTION` where date between :fromTimestamp-1 and :toTimestamp")
    fun getTransactionsBetweenTimestamp(fromTimestamp: Long, toTimestamp: Long) : List<TransactionDataModel>

    @Insert
    fun addTransaction(transactionDataModel: TransactionDataModel)

    @Query("delete from `$TABLE_TRANSACTION`")
    fun deleteAllTransactions()

    @Query("delete from `$TABLE_TRANSACTION` where id = :transactionId")
    fun deleteTransactionById(transactionId: Int)
}