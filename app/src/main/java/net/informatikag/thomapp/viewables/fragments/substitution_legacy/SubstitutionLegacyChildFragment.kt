package net.informatikag.thomapp.viewables.fragments.substitution_legacy

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.SubstitutionLegacySubFragmentBinding

class SubstitutionLegacyChildFragment : Fragment() {

    private var _binding: SubstitutionLegacySubFragmentBinding? = null   // Binding to access the layout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val URL_KEY = "url"
    private var pdfURL: String? = null
    private var loadingError:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = SubstitutionLegacySubFragmentBinding.inflate(inflater, container, false)

        // Get URL from Preferences
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        arguments?.let {
            pdfURL = sharedPreferences.getString(it.getString(URL_KEY), "")
        }

        // Setup Refresh Button
        binding.substitutionRefreshButton.setOnClickListener {
            binding.substitutionPdfView.reload()
            binding.substitutionPdfView.visibility = View.GONE
            binding.substitutiionProgressbar.visibility = View.VISIBLE
            loadingError = false
        }

        // Setup Webview and Load Page
        binding.substitutionPdfView.settings.loadWithOverviewMode = true
        binding.substitutionPdfView.settings.javaScriptEnabled = true
        binding.substitutionPdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfURL")
        binding.substitutionPdfView.webViewClient = SubstitutionLegacyWebViewClient()

        // Return layout
        return binding.root
    }

    /**
     * If the fragment is no longer used, the binding must also be removed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(urlKeyName: String) =
            SubstitutionLegacyChildFragment().apply {
                arguments = Bundle().apply {
                    putString(URL_KEY, urlKeyName)
                }
            }
    }

    inner class SubstitutionLegacyWebViewClient: WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            //only execute if there was no error while loading
            if (!loadingError) {
                //Show Webview and remove Progressbar
                binding.substitutionPdfView.visibility = View.VISIBLE
                binding.substitutiionProgressbar.visibility = View.GONE
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
            binding.substitutionPdfView.visibility = View.GONE
            binding.substitutiionProgressbar.visibility = View.GONE

            // Create and display snack bar
            Snackbar.make(
                requireActivity().findViewById(R.id.app_bar_main),
                "Es gab einen Fehler wärend des Ladens${if(error != null) ": ${error.description}" else null}", Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }
    }
}