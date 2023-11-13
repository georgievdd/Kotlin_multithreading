import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

object globalStringBuffer {
    val logMessages = LinkedBlockingQueue<String>()
    fun listen() = ListenStream().start()
}

class ListenStream : Thread() {
    override fun run() {
        while (true) {
            val message = globalStringBuffer.logMessages.take()
            print(message)
        }
    }
}

interface Observer {
    fun update(message: String)
}

class Logger(val prefix: String) : Observer {

    private val WIDTH = 200
    private fun log(message: String) {
        val sep = "".padEnd(WIDTH, '*')
        val time = "[${SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())}] *"
        val pref = "* [" + prefix + "] "
        val row = pref + message.padEnd(Math.max(0, sep.length - time.length - pref.length), ' ') + time
        val logMessage =
            sep + '\n' +
            '*' + "".padEnd(WIDTH - 2, ' ') + '*' + '\n' +
            row  + '\n' +
            '*' + "".padEnd(WIDTH - 2, ' ') + '*' + '\n' +
            sep + '\n'
        globalStringBuffer.logMessages.add(logMessage)
    }

    override fun update(message: String) {
        synchronized(this) {
            log(message)
        }
    }
}