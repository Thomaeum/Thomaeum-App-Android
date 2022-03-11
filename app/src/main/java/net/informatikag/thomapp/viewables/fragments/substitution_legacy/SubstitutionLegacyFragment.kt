package net.informatikag.thomapp.viewables.fragments.substitution_legacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.SubstitutionFragmentMainBinding
import net.informatikag.thomapp.databinding.SubstitutionLegacyFragmentBinding
import net.informatikag.thomapp.utils.models.view.SubstitutionViewModel
import net.informatikag.thomapp.viewables.fragments.substitution.SubstitutionPagerAdapter

class SubstitutionLegacyFragment : Fragment() {

    private var _binding: SubstitutionLegacyFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SubstitutionLegacyFragmentBinding.inflate(inflater, container, false)  //Layout aufbauen

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.substitutionLegacyTabLayout.setupWithViewPager(binding.substitutionLegacyViewPager)
        binding.substitutionLegacyViewPager.adapter = SubstitutionLegacyPagerAdapter(childFragmentManager, requireContext())
    }
}