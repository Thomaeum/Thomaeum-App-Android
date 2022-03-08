package net.informatikag.thomapp.viewables.fragments.substitution

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.informatikag.thomapp.databinding.SubstitutionFragmentMainBinding

class SubstitutionFragment : Fragment() {

    private var _binding: SubstitutionFragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SubstitutionFragmentMainBinding.inflate(inflater, container, false)  //Layout aufbauen

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.substitutionTabLayout.setupWithViewPager(binding.substitutionViewPager)
        binding.substitutionViewPager.adapter = SubstitutionPagerAdapter(childFragmentManager)
    }
}