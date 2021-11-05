package net.informatikag.thomapp.vertretungsplan.utils

import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.informatikag.thomapp.databinding.FragmentVertretungsplanTemplateBinding

class VertretungsplanHandler(
    pdfURL: String,
    layout: FragmentVertretungsplanTemplateBinding
) {
    init {
        layout.vertretungsplanOberstufePdfView.settings.loadWithOverviewMode = true
        layout.vertretungsplanOberstufePdfView.settings.javaScriptEnabled = true
        layout.vertretungsplanOberstufePdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfURL")
    }
}