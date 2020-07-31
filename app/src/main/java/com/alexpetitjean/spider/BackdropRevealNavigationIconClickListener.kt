package com.alexpetitjean.spider

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat

class BackdropRevealNavigationIconClickListener(
    activity: Activity,
    private val frontLayer: View,
    private val calculateBackLayerHeight: () -> Int
) : View.OnClickListener {

    private val context: Context = activity
    private val height: Int
    private val duration = context.resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    private val openIcon: Drawable
    private val closeIcon: Drawable

    private var animatorSet = AnimatorSet()
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels

        openIcon = ContextCompat.getDrawable(context, R.drawable.ic_menu_black_24dp)!!
        closeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp)!!
    }

    override fun onClick(view: View) {
        backdropShown = !backdropShown

        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        if (view is ImageView) {
            updateIcon(view)
        }

        view.requestFocus()

        val translateY = if (backdropShown) {
            calculateBackLayerHeight()
        } else {
            0
        }.toFloat()

        val animator = ObjectAnimator.ofFloat(frontLayer, "translationY", translateY)
        animator.duration = duration
        animator.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(animator)
        animator.start()
    }

    private fun updateIcon(imageView: ImageView) {
        if (backdropShown) {
            imageView.setImageDrawable(closeIcon)
        } else {
            imageView.setImageDrawable(openIcon)
        }
    }
}
