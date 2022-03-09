package net.informatikag.thomapp.viewables.fragments.substitution_legacy

import android.content.SharedPreferences
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.databinding.SubstitutionLegacyTemplateFragmentBinding

/**
 * Handles a Substitution plan View
 */
class SubstitutionHandler(
    val urlPreferencesKey: String,
    val layout: SubstitutionLegacyTemplateFragmentBinding,
    val snackbarView: CoordinatorLayout
): WebViewClient() {

    private var loadingError:Boolean = false

    init {
        // Setup Refresh Button
        layout.substitutionRefreshButton.setOnClickListener {
            layout.substitutionPdfView.reload()
            layout.substitutionPdfView.visibility = View.GONE
            layout.substitutiionProgressbar.visibility = View.VISIBLE
            loadingError = false
        }

        // Get URL from Preferences
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(layout.root.context)

        // Setup Webview and Load Page
        layout.substitutionPdfView.settings.loadWithOverviewMode = true
        layout.substitutionPdfView.settings.javaScriptEnabled = true
        layout.substitutionPdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=${sharedPreferences.getString(urlPreferencesKey, "")}")
        layout.substitutionPdfView.webViewClient = this
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        //only execute if there was no error while loading
        if (!loadingError) {
            //Show Webview and remove Progressbar
            layout.substitutionPdfView.visibility = View.VISIBLE
            layout.substitutiionProgressbar.visibility = View.GONE
        }
    }

    /**
     * If there is an error while loading the webview it is represented by a snachbar
     */
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        // Prevents the webview from being displayed
        loadingError = true

        // Hide Webview and Progressbar
        layout.substitutionPdfView.visibility = View.GONE
        layout.substitutiionProgressbar.visibility = View.GONE

        // Create and display snack bar
        Snackbar.make(snackbarView, "Es gab einen Fehler wärend des Ladens${if(error != null) ": ${error.description}" else null}", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }
}