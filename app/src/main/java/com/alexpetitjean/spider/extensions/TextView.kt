package com.alexpetitjean.spider.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.onTextChanged(onTextChanged: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
            onTextChanged(charSequence.toString())
        }
        override fun afterTextChanged(p0: Editable?) = Unit
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    })
}
