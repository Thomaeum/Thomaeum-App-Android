package net.informatikag.thomapp.ui.screens.vertretungsplan.oberstufe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.informatikag.thomapp.databinding.FragmentVertretungsplanOberstufeBinding
import net.informatikag.thomapp.ui.gallery.VertretungsplanOberstufeViewModel

class VertretungsplanOberstufeFragment : Fragment() {

    private lateinit var vertretungsplanOberstufeViewModel: VertretungsplanOberstufeViewModel
    private var _binding: FragmentVertretungsplanOberstufeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVertretungsplanOberstufeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vertretungsplanOberstufeViewModel = ViewModelProvider(this).get(VertretungsplanOberstufeViewModel::class.java)

        val pdfView: WebView = binding.vertretungsplanOberstufePdfView
        pdfView.settings.loadWithOverviewMode = true
        pdfView.settings.javaScriptEnabled = true
        vertretungsplanOberstufeViewModel.url.observe(viewLifecycleOwner, Observer {
            pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=$it")
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}