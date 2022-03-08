package net.informatikag.thomapp.viewables.fragments.substitution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import net.informatikag.thomapp.databinding.SubstitutionFragmentPageBinding
import net.informatikag.thomapp.utils.handlers.SubstitutionListAdapter
import net.informatikag.thomapp.utils.models.view.VertretungsplanViewModel

class SubstitutionListFragment: Fragment() {

    private var _binding: SubstitutionFragmentPageBinding? = null
    private val viewModel: VertretungsplanViewModel by activityViewModels()   // Viewmodel für den Vertretungsplan

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SubstitutionFragmentPageBinding.inflate(inflater, container, false)  //Layout aufbauen

        //Vertretungsplan
        val listView = binding.substitutionListView
        val listViewAdapter = SubstitutionListAdapter(this.requireContext(), viewModel)
        viewModel.entrys.observe(viewLifecycleOwner, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listView.adapter = listViewAdapter
        if(viewModel.isEmpty()) viewModel.loadVertretunsplan(requireContext(), this.requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("day") }?.apply {
            // TODO Update Layout from Viewmodel
            viewModel.getByDay(getInt("day"))

        }
    }
}