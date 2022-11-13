package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("select * from `$TABLE_TRANSACTION`")
    fun getAllTransactions() : List<TransactionDataModel>

    @Insert
    fun addTransaction(transactionDataModel: TransactionDataModel)

    @Query("delete from `$TABLE_TRANSACTION`")
    fun deleteAllTransactions()
}