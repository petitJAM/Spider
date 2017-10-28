package com.alexpetitjean.spider

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.util.PatternsCompat
import android.view.Menu
import android.view.MenuItem

fun isValidUrl(url: String) = PatternsCompat.WEB_URL.matcher(url).matches()

/**
 * Stolen from here:
 * https://stackoverflow.com/a/33697621/5577371
 */
fun tintAllIcons(menu: Menu, @ColorInt color: Int) {
    @Suppress("LoopToCallChain")
    for (i in 0 until menu.size()) {
        val item = menu.getItem(i)
        tintMenuItemIcon(item, color)
    }
}

private fun tintMenuItemIcon(item: MenuItem, @ColorInt color: Int) {
    item.icon?.let { drawable: Drawable ->
        val wrapped = DrawableCompat.wrap(drawable)
        drawable.mutate()
        DrawableCompat.setTint(wrapped, color)
        item.icon = drawable
    }
}
