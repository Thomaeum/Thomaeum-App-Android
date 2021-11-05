package net.informatikag.thomapp.vertretungsplan.oberstufe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.FragmentVertretungsplanOberstufeBinding
import net.informatikag.thomapp.vertretungsplan.utils.VertretungsplanHandler

class VertretungsplanOberstufeFragment : Fragment() {

    private var _binding: FragmentVertretungsplanOberstufeBinding? = null
    private lateinit var handler: VertretungsplanHandler

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

        handler = VertretungsplanHandler(
            "https://thomaeum.de/wp-content/uploads/2020/10/thom2.pdf",
            binding.fragmentVertretungsplanLayout
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}