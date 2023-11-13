data class ErrorApi(override var message: String) : Error()  {

    companion object {
        fun insufficientFndsError(): ErrorApi {
            return ErrorApi("Недостаточно средств")
        }
        fun insufficientFndsForTransferError(userId: String): ErrorApi {
            return ErrorApi("Недостаточно средств для перевода пользователю $userId")
        }
    }

}