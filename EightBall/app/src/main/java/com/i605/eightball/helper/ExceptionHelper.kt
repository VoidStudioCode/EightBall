package com.i605.eightball.helper

import java.text.SimpleDateFormat
import java.util.*
import java.io.*
import android.widget.Toast
import android.os.Looper
import android.util.Log
import com.i605.eightball.*

object ExceptionHelper : Thread.UncaughtExceptionHandler {
    private val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA)
    private val mDefaultHandler: Thread.UncaughtExceptionHandler by lazy { Thread.getDefaultUncaughtExceptionHandler() }

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, e: Throwable) {
        if (!handleException(e)) {
            mDefaultHandler.uncaughtException(thread, e)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e2: InterruptedException) {
                e2.printStackTrace()
            }
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    fun handleException(e: Throwable?): Boolean {
        if (e == null) {
            return false
        }
        Thread {
            Looper.prepare()
            Toast.makeText(App.getAppContext(), R.string.error_occur, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()
        Log.e("error", e.toString())
        saveCrashInfo2File(e)
        return true
    }

    private fun saveCrashInfo2File(e: Throwable) {
        val sb = StringBuilder()
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        val error = sb.toString().replace("\n", "\r\n")
        val timestamp = System.currentTimeMillis()
        val time = formatter.format(System.currentTimeMillis())
        val fileName = "FC-$time-$timestamp.txt"
        File("${App.getAppContext().getExternalFilesDir(null)?.absolutePath}/ErrorLog").mkdirs()
        val file = File("${App.getAppContext().getExternalFilesDir(null)?.absolutePath}/ErrorLog/$fileName")
        file.createNewFile()
        val fileWriter = FileWriter(file)
        fileWriter.write(error)
        fileWriter.flush()
        fileWriter.close()
    }
}