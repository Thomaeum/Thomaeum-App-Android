package net.informatikag.thomapp.viewables.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.HomeFragmentBinding
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder

/**
 * No idea what will be displayed here, currently there is not really something here
 */
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null   // Binding um auf das Layout zuzugreifen

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var articleViewHolder:ThomsLineArticleViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)  //Layout aufbauen
        articleViewHolder = ThomsLineArticleViewHolder(binding.homeArticlePreview.root, this)
        return binding.root
    }

    // Wird ausgeführt wenn das Fragment entladen wird
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}