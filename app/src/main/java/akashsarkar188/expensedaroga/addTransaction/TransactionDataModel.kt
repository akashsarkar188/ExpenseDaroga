package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION
import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION_META
import androidx.room.ColumnInfo
import androidx.room.Embedded
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
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "date") var date: String,
    @Embedded var meta: TransactionMeta?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
) {
    data class TransactionMeta(
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "transactionType") var transactionType: TransactionType
    )
}
