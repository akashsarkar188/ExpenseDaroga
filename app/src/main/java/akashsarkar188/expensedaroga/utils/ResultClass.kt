package akashsarkar188.expensedaroga.utils

data class ResultClass(
    var error: ErrorModel?,
    var success: Any?
) {
    data class ErrorModel(
        var errorMessage : String
    )
}
