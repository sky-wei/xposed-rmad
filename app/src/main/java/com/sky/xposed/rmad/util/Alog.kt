package com.sky.xposed.rmad.util

import android.util.Log

/**
 * Created by sky on 17-11-1.
 */
object Alog {

    val tag = "Xposed"
    var debug = true

    fun d(msg: String) {
        d(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (debug) Log.d(tag, msg)
    }

    fun d(msg: String, tr: Throwable) {
        d(tag, msg, tr)
    }

    fun d(tag: String, msg: String, tr: Throwable) {
        if (debug) Log.d(tag, msg, tr)
    }

    fun e(msg: String) {
        e(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (debug) Log.e(tag, msg)
    }

    fun e(msg: String, tr: Throwable) {
        e(tag, msg, tr)
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        if (debug) Log.e(tag, msg, tr)
    }
}