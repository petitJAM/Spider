package com.alexpetitjean.spider.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.requestFocusAndOpenKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(applicationWindowToken, InputMethodManager.SHOW_FORCED, 0)
}

/**
 * This will only work if `this` is the view that is currently focused. I think.
 * The soft keyboard API is garbage on Android.
 */
fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
