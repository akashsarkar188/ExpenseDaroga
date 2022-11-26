package akashsarkar188.expensedaroga.screens.addTransaction.model

import akashsarkar188.expensedaroga.utils.TABLE_TRANSACTION
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

enum class TransactionType {
    CREDIT,
    DEBIT,
    LOAN_TAKEN,
    LOAN_GIVEN
}

enum class TransactionCategories {
    daily_expense
}

@Entity(tableName = TABLE_TRANSACTION)
data class TransactionDataModel(
    @ColumnInfo(name = "amount") var amount: Double? = null,
    @ColumnInfo(name = "date") var date: Long? = null,
    @Embedded var meta: TransactionMeta? = null,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
) {
    data class TransactionMeta(
        @ColumnInfo(name = "category") var category: String? = null,
        @ColumnInfo(name = "transactionType") var transactionType: TransactionType? = null,
        @ColumnInfo(name = "note") var transactionNote: String? = null
    )
}
