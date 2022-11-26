package akashsarkar188.expensedaroga.screens.addTransaction.model

import akashsarkar188.expensedaroga.R

data class TransactionTypeModel(
    var type: TransactionType = TransactionType.DEBIT,
    var isSelected: Boolean = false,
    var selectedColor: Int = R.color.red_400
)
