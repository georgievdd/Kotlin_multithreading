import java.util.*


class Client(val bank: Bank) {
    val id = UUID.randomUUID().toString()
    @Volatile var amount = 0.0
    @Volatile var currency = 0.0

    fun addAmount(value: Double) {
        synchronized(this) {
            amount += value
        }
    }

    fun addCurrency(value: Double) {
        synchronized(this) {
            currency += value
        }
    }


    fun exchangeCurrency(fromCurrency: String, toCurrency: String, currency: Double) {
        bank.exchangeCurrency(id, fromCurrency, toCurrency, currency)
    }

    fun transferFunds(receiverId: String, amount: Double) {
        bank.transferFunds(id, receiverId, amount)
    }

    fun withdraw(amount: Double) {
        bank.withdraw(id, amount)
    }

    fun deposit(amount: Double) {
        bank.deposit(id, amount)
    }

}