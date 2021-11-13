package net.informatikag.thomapp.viewables.fragments.vertretungsplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.VertretungsplanOberstufeFragmentBinding

/**
 * Zeigt den Oberstufen Vertretungsplan an, wobei die eigendlichen funktionalen Teile dieser Klasse
 * in VertretunsplanHandler ausgelagert wurde, da sie sich nur durch die URL zum Plan unterscheiden
 */
class VertretungsplanOberstufeFragment : Fragment() {

    private var _binding: VertretungsplanOberstufeFragmentBinding? = null   // Binding um auf das Layout zuzugreifen
    private lateinit var handler: VertretungsplanHandler                    // Die funktionalen Teile

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = VertretungsplanOberstufeFragmentBinding.inflate(inflater, container, false)

        // Handler hinzufügen
        handler = VertretungsplanHandler(
            "https://thomaeum.de/wp-content/uploads/2020/10/thom2.pdf",
            binding.fragmentVertretungsplanLayout,
            requireActivity().findViewById(R.id.app_bar_main)
        )

        // Layout zurückgeben
        return binding.root
    }

    /**
     * Wenn das Fragment nicht mehr verwendet wird muss das Binding auch weg
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}