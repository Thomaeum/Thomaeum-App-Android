package net.informatikag.thomapp.vertretungsplan.utils

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.FragmentVertretungsplanTemplateBinding

class VertretungsplanHandler(
    pdfURL: String,
    val layout: FragmentVertretungsplanTemplateBinding,
    val snackbarView: CoordinatorLayout
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
        layout.root.post{ layout.root.isRefreshing = true }
    }

    override fun onRefresh() {
        layout.vertretungsplanOberstufePdfView.reload()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        layout.root.post{ layout.root.isRefreshing = false }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        //TODO add Error Code
        Snackbar.make(snackbarView, "Es gab einen Fehler wärend des Ladens", Snackbar.LENGTH_LONG).show()
        layout.root.post { layout.root.isRefreshing = false }
    }
}