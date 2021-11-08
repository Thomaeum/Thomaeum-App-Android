package net.informatikag.thomapp.viewables.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.VertretungsplanOberstufeFragmentBinding
import net.informatikag.thomapp.utils.handlers.VertretungsplanHandler

class VertretungsplanUnterstufeFragment : Fragment() {

    private var _binding: VertretungsplanOberstufeFragmentBinding? = null
    private lateinit var handler: VertretungsplanHandler

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = VertretungsplanOberstufeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        handler = VertretungsplanHandler(
            "https://thomaeum.de/wp-content/uploads/2020/10/thom_si.pdf",
            binding.fragmentVertretungsplanLayout,
            requireActivity().findViewById(R.id.app_bar_main)
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}