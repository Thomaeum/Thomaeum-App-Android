package net.informatikag.thomapp.viewables.fragments.substitution_legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.SubstitutionLegacyOberstufeFragmentBinding

/**
 * Displays the lower level substitution plan, where the actual functional parts of this class have
 * been moved to SubstitutionHandler, since they differ only by the URL to the plan.
 */
class SubstitutionUnterstufeFragment : Fragment() {

    private var _binding: SubstitutionLegacyOberstufeFragmentBinding? = null   // Binding to access the layout
    private lateinit var handler: SubstitutionHandler                          // The functional parts

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = SubstitutionLegacyOberstufeFragmentBinding.inflate(inflater, container, false)

        // Add handler
        handler = SubstitutionHandler(
            "preferences_substitution_legacy_url_unterstufe",
            binding.fragmentSubstitutionLayout,
            requireActivity().findViewById(R.id.app_bar_main)
        )

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
}