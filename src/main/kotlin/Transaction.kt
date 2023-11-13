import java.util.*

abstract class Transaction(val observer: Observer) {
    val id = UUID.randomUUID().toString()
    open fun start() {}
    fun log(message: String) {
        observer.update(message)
    }
}


//currency - валюта для пополнения, amount - сумма вычета из основного счета
class TransactionExchangeCurrency(
    val currency: Double,
    val client: Client,
    val amount: Double,
    observer: Observer,
) : Transaction(observer) {
    override fun start() {
        try {
            if (client.amount < amount) {
                throw ErrorApi.insufficientFndsError()
            }
            client.addAmount(-amount)
            client.addCurrency(currency)
            this.log("Операция прошла успешно!")
        } catch (e: ErrorApi) {
            this.log("Ошибка! ${e.message}")
        }
    }
}

class TransactionTransferFunds(
    val clientFrom: Client,
    val clientTo: Client,
    val amount: Double,
    observer: Observer,
) : Transaction(observer) {
    override fun start() {
        try {
            if (clientFrom.amount < amount) {
                throw ErrorApi.insufficientFndsForTransferError(clientTo.id)
            }
            clientFrom.addAmount(-amount)
            clientTo.addAmount(amount)
            this.log("Операция прошла успешно!")
        } catch (e: ErrorApi) {
            this.log("Ошибка! ${e.message}")
        }
    }
}

class TransactionDeposit(
    val client: Client,
    val amount: Double,
    observer: Observer,
) : Transaction(observer) {
    override fun start() {
        try {
        client.addAmount(amount)
            this.log("Операция прошла успешно!")
        } catch (e: ErrorApi) {
            this.log("Ошибка! ${e.message}")
        }
    }
}

class TransactionWithdraw(
    val client: Client,
    val amount: Double,
    observer: Observer,
) : Transaction(observer) {
    override fun start() {
        try {
        if (client.amount < amount) {
            throw ErrorApi.insufficientFndsError()
        }
        client.addAmount(-amount)
            this.log("Операция прошла успешно!")
        } catch (e: ErrorApi) {
            this.log("Ошибка! ${e.message}")
        }
    }
}