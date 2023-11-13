



class Cashier(val bank: Bank) : Thread() {
    override fun run() {
        while (true) {
            try {
                val transaction = bank.transactionQueue.take()
                transaction.start()
            } catch (e: InterruptedException) {
                //Thread.currentThread().interrupt()
                break
            }
        }
    }
}