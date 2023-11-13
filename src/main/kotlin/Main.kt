import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue

fun main(args: Array<String>) {

    globalStringBuffer.listen()

    val clientsIdBuffer = mutableListOf<String>()

    val Sberbank = Bank()
    for (i in 0..<10) {
        Sberbank.addClient(Client(Sberbank))
    }
    for (client in Sberbank.clients.values) {
        Sberbank.addObserver(client.id)
    }
    Sberbank.clients.values.forEach {
        it.deposit(0.0)
        clientsIdBuffer.add(it.id)
    }



    while (true) {

        runBlocking {
            for (client in Sberbank.clients.values) {
                when ((Math.random() * 4).toInt()) {
                    0 -> client.exchangeCurrency("USD", "RUB", Math.random() * 100)
                    1 -> client.withdraw(Math.random() * 500)
                    2 -> client.deposit(Math.random() * 500)
                    3 -> client.transferFunds(clientsIdBuffer[(Math.random() * clientsIdBuffer.size).toInt()], Math.random() * 300)
                }
            }
        }

        Thread.sleep(1000)
    }

}