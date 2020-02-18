package com.gu.toolargetool

import android.util.Log

/**
 * Interface that allows flexibility in how TooLargeTool's output is logged. The default
 * implementation [LogcatLogger] should be suitable in most cases.
 */
interface Logger {
    fun log(msg: String)
    fun logTransactionTooLargeDetected(msg: String)
    fun logException(e: Exception)
}

/**
 * The default implementation of [Logger].
 *
 * @author [@sfriedenberg](https://github.com/friedenberg)
 */
class LogcatLogger(private val priority: Int = Log.DEBUG, private val tag: String = "TooLargeTool") : Logger {

    override fun log(msg: String) {
        Log.println(priority, tag, msg)
    }

    override fun logException(e: Exception) {
        Log.w(tag, e.message, e)
    }

    override fun logTransactionTooLargeDetected(msg: String) {
        Log.e(tag, "TransactionTooLarge detected: $msg")
    }
}
