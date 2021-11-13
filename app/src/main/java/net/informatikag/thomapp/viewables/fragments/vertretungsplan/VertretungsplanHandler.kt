package net.informatikag.thomapp.viewables.fragments.vertretungsplan

import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.databinding.VertretungsplanTemplateFragmentBinding

class VertretungsplanHandler(
    pdfURL: String,
    val layout: VertretungsplanTemplateFragmentBinding,
    val snackbarView: CoordinatorLayout
): WebViewClient() {

    private var loadingError:Boolean = false

    init {
        //Setup Refresh Button
        layout.vertretungsplanRefreshButton.setOnClickListener {
            layout.vertretungsplanPdfView.reload()
            layout.vertretungsplanPdfView.visibility = View.GONE
            layout.vertretungsplanProgressbar.visibility = View.VISIBLE
            loadingError = false
        }

        //Setup Webview and Load Page
        layout.vertretungsplanPdfView.settings.loadWithOverviewMode = true
        layout.vertretungsplanPdfView.settings.javaScriptEnabled = true
        layout.vertretungsplanPdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfURL")
        layout.vertretungsplanPdfView.webViewClient = this
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        //only execute if there was no error while loading
        if (!loadingError) {
            //Show Webview and remove Progressbar
            layout.vertretungsplanPdfView.visibility = View.VISIBLE
            layout.vertretungsplanProgressbar.visibility = View.GONE
        }
    }

    /**
     * Wenn es einen Fehler beim Laden des Webviews gibt wird dies durch eine Snachbar dargestellt
     */
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        // Verhindert das der Webview angezeigt wird
        loadingError = true

        // Webview und Progressbar verstecken
        layout.vertretungsplanPdfView.visibility = View.GONE
        layout.vertretungsplanProgressbar.visibility = View.GONE

        // Snackbar erstellen und anzeigen
        Snackbar.make(snackbarView, "Es gab einen Fehler wärend des Ladens${if(error != null) ": ${error.description}" else null}", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }
}