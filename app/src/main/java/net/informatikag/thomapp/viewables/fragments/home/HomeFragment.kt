package net.informatikag.thomapp.viewables.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.databinding.HomeFragmentBinding

/**
 * Keine Ahnung was hier mal angezeigt wird
 */
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null   // Binding um auf das Layout zuzugreifen

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)  //Layout aufbauen
        binding.textHome.text = "Coming soon!"  // Default Text einfügen
        return binding.root
    }

    // Wird ausgeführt wenn das Fragment entladen wird
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}