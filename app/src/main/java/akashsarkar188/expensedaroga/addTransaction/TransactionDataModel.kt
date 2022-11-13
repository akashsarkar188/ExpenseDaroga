package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION
import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION_META
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    CREDIT,
    DEBIT,
    LOAN_TAKEN,
    LOAN_GIVEN
}

@Entity(tableName = TABLE_TRANSACTION)
data class TransactionDataModel(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "date") var date: String,
) {

    /*@Entity(tableName = TABLE_TRANSACTION_META)
    data class TransactionMeta(
        @PrimaryKey(autoGenerate = true) var id: Int,
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "transactionType") var transactionType: TransactionType
    )*/
}
