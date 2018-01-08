package com.alexpetitjean.spider.extensions

import android.view.View
import android.widget.LinearLayout

val LinearLayout.children: List<View>
    get () = (0 until childCount).map { i -> getChildAt(i) }
