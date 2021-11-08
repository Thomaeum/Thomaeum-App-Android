package net.informatikag.thomapp.vertretungsplan.utils

import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.databinding.FragmentVertretungsplanTemplateBinding

class VertretungsplanHandler(
    pdfURL: String,
    val layout: FragmentVertretungsplanTemplateBinding,
    val snackbarView: CoordinatorLayout
): WebViewClient() {
    init {
        //Setup Refresh Button
        layout.vertretungsplanRefreshButton.setOnClickListener {
            layout.vertretungsplanPdfView.reload()
            layout.vertretungsplanPdfView.visibility = View.GONE
            layout.vertretungsplanProgressbar.visibility = View.VISIBLE
        }

        //Setup Webview and Load Page
        layout.vertretungsplanPdfView.settings.loadWithOverviewMode = true
        layout.vertretungsplanPdfView.settings.javaScriptEnabled = true
        layout.vertretungsplanPdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfURL")
        layout.vertretungsplanPdfView.webViewClient = this
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        //Show Webview and remove Progressbar
        layout.vertretungsplanPdfView.visibility = View.VISIBLE
        layout.vertretungsplanProgressbar.visibility = View.GONE
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        Snackbar.make(snackbarView, "Es gab einen Fehler wärend des Ladens${if(error != null) ": ${error.description}" else null}", Snackbar.LENGTH_LONG).show()
        layout.vertretungsplanPdfView.visibility = View.GONE
        layout.vertretungsplanPdfView.visibility = View.GONE
    }
}