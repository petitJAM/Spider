package com.alexpetitjean.spider

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_web_view_controls_backdrop.view.*
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.view.*

class WebViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backdropContentTopMargin = requireContext().resources
            .getDimension(R.dimen.backdropContentTopMargin)
            .toInt()
        view.webViewToolbar.setNavigationOnClickListener(
            BackdropRevealNavigationIconClickListener(
                requireActivity(),
                view.webViewFrontLayer
            ) {
                backdropContentTopMargin * 2 + view.controlsBackdropContent.height
            }
        )

        val colorOnPrimary = requireContext().getThemedColor(R.attr.colorOnPrimary)
        view.webViewToolbar.menu.tintAllIcons(colorOnPrimary)

        view.webViewToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.controlsRefresh -> {
                    view.webView.reload()
                    true
                }
                R.id.controlsBack -> {
                    if (view.webView.canGoBack()) {
                        view.webView.goBack()
                        true
                    } else {
                        false
                    }
                }
                R.id.controlsForward -> {
                    if (view.webView.canGoForward()) {
                        view.webView.goForward()
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }

        view.webView.settings.apply {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
        }

        view.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webViewLoadingIndicator.isVisible = true
                webViewFavicon.isVisible = false
                webViewFavicon.setImageBitmap(favicon)
                webViewTitle.setText(R.string.web_view_title_loading)
                webViewUrl.text = url
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webViewLoadingIndicator.isVisible = false
                webViewFavicon.isVisible = true
                webViewFavicon.setImageBitmap(view?.favicon)
                webViewTitle.text = view?.title
                webViewUrl.text = url
            }
        }

        view.webView.loadUrl("https://google.com/search?q=bionicle&tbm=isch")
    }
}
