package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.utils.Constants
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

enum class TransactionType {
    CREDIT,
    DEBIT,
    LOAN_TAKEN,
    LOAN_GIVEN
}

@Entity(tableName = Constants.TABLE_TRANSACTION)
data class TransactionDataModel(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var amount: Double,
    var date: String,
) {

    @Entity(tableName = Constants.TABLE_TRANSACTION_META)
    data class TransactionMeta(
        var category: String,
        var transactionType: TransactionType
    )
}
