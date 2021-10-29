package net.informatikag.thomapp.ui.thomsline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import net.informatikag.thomapp.ui.gallery.ThomsLineViewModel

class ThomsLineFragment : Fragment(){

    private lateinit var thomsLineViewModel: ThomsLineViewModel
    private var _binding: FragmentThomslineBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thomsLineViewModel =
            ViewModelProvider(this).get(ThomsLineViewModel::class.java)

        _binding = FragmentThomslineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textThomsline
        thomsLineViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}