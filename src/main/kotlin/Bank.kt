import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class Bank {
    val clients = ConcurrentHashMap<String, Client>()
    val cashiers = ArrayList<Cashier>()
    val exchangeRates = ConcurrentHashMap<String, Double>()
    val transactionQueue = LinkedBlockingQueue<Transaction>()
    private val observers = mutableListOf<Observer>()

    fun addClient(client: Client) {
        clients.put(client.id, client)
    }

    fun addObserver(userId: String) {
        observers.add(Logger(prefix = "Оповещение для клиента ${userId}"))
    }

    fun notifyObservers(message: String) {
        observers.forEach {
            it.update(message)
        }
    }

    init {
        fun initCurrency() {
            exchangeRates["USD"] = 1.0
            exchangeRates["AED"] = 3.67
            exchangeRates["ARS"] = 64.51
            exchangeRates["AUD"] = 1.65
            exchangeRates["CAD"] = 1.42
            exchangeRates["RUB"] = 91.42
        }
        fun initCashiers() {
            cashiers.add(Cashier(this))
        }
        fun updateCurrency() {
            synchronized(exchangeRates) {
                for ((currency) in exchangeRates) {
                    exchangeRates[currency] = Math.random() * 100
                }
                notifyObservers("Курсы валют были изменены!!!")
            }
        }
        initCurrency()
        initCashiers()

        val executor = ScheduledThreadPoolExecutor(1)
        executor.scheduleAtFixedRate({ updateCurrency() }, 0, 10, TimeUnit.SECONDS)
        initCurrency()
        initCashiers()
        for (cashier in cashiers) {
            cashier.start()
        }
    }

    fun exchangeCurrency(clientId: String, fromCurrency: String, toCurrency: String, currency: Double) {
        val amount = exchangeRates[toCurrency]!! / exchangeRates[fromCurrency]!! * currency
        transactionQueue.put(TransactionExchangeCurrency(
            currency = currency,
            client = clients[clientId]!!,
            amount = amount,
            observer = Logger("${clientId}: Перевод валюты")
        ))
    }

    fun transferFunds(senderId: String, receiverId: String, amount: Double) {
        transactionQueue.put(TransactionTransferFunds(
            clientFrom = clients[senderId]!!,
            clientTo = clients[receiverId]!!,
            amount = amount,
            observer = Logger("${senderId}: Перевод на счет клиента ${receiverId}")
        ))
    }

    fun withdraw(clientId: String, amount: Double) {
        transactionQueue.put(TransactionWithdraw(
            client = clients[clientId]!!,
            amount = amount,
            observer = Logger("${clientId}: Снятие средств")
        ))
    }

    fun deposit(clientId: String, amount: Double) {
        transactionQueue.put(TransactionDeposit(
            client = clients[clientId]!!,
            amount = amount,
            observer = Logger("${clientId}: Пополнение счета")
        ))
    }
}