package net.informatikag.thomapp.vertretungsplan.utils

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.FragmentVertretungsplanTemplateBinding

class VertretungsplanHandler(
    pdfURL: String,
    val layout: FragmentVertretungsplanTemplateBinding
): SwipeRefreshLayout.OnRefreshListener, WebViewClient() {
    init {
        layout.vertretungsplanOberstufePdfView.settings.loadWithOverviewMode = true
        layout.vertretungsplanOberstufePdfView.settings.javaScriptEnabled = true
        layout.vertretungsplanOberstufePdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfURL")
        layout.vertretungsplanOberstufePdfView.webViewClient = this


        layout.root.setOnRefreshListener(this)
        layout.root.setColorSchemeResources(
            R.color.primaryColor,
            R.color.secondaryColor
        )
        layout.root.post{
            layout.root.isRefreshing = true
        }
    }

    override fun onRefresh() {
        layout.vertretungsplanOberstufePdfView.reload()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        layout.root.post{
            layout.root.isRefreshing = false
        }
    }
}