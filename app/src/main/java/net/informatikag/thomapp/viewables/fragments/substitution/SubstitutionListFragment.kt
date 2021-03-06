package net.informatikag.thomapp.viewables.fragments.substitution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.informatikag.thomapp.databinding.SubstitutionFragmentPageBinding
import net.informatikag.thomapp.utils.models.view.SubstitutionViewModel

class SubstitutionListFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: SubstitutionFragmentPageBinding? = null
    private val viewModel: SubstitutionViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SubstitutionFragmentPageBinding.inflate(inflater, container, false)  //Layout aufbauen

        binding.root.setOnRefreshListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("day") }?.apply {
            viewModel.initListView(binding.substitutionListView, binding.substitutionEmptyText, requireContext(), viewLifecycleOwner, requireActivity(), getInt("day"), true)
        }
    }

    override fun onRefresh() {
        viewModel.loadSubstitution(requireContext(),requireActivity(), true){ res, err -> binding.root.isRefreshing = false}
    }
}