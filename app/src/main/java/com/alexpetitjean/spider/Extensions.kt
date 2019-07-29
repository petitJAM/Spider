package com.alexpetitjean.spider

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getThemedColor(@AttrRes attribute: Int): Int {
    return TypedValue().let {
        theme.resolveAttribute(attribute, it, true)
        it.data
    }
}

/**
 * Stolen from here:
 * https://stackoverflow.com/a/33697621/5577371
 */
fun Menu.tintAllIcons(@ColorInt color: Int) {
    @Suppress("LoopToCallChain")
    for (i in 0 until size()) {
        val item = getItem(i)
        item.icon?.let { drawable: Drawable ->
            val wrapped = DrawableCompat.wrap(drawable)
            drawable.mutate()
            DrawableCompat.setTint(wrapped, color)
            item.icon = drawable
        }
    }
}
