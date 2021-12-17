package net.informatikag.thomapp.viewables.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.HomeFragmentBinding
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle
import net.informatikag.thomapp.viewables.fragments.ThomsLine.main.ThomsLineFragmentDirections
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder

/**
 * No idea what will be displayed here, currently there is not really something here
 */
class HomeFragment : Fragment(), ArticleClickHandler{

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

        //TODO Load this From API
        val post:ThomsLineWordpressArticle = ThomsLineWordpressArticle(
            1843,
            "ja Moin",
            "https://cdn.pixabay.com/photo/2013/07/12/17/47/test-pattern-152459_960_720.png",
            "Voll Nice Hier",
            null,
            null,
            null,
            true,
            true
        )
        
        articleViewHolder!!.bind(post, this);

        return binding.root
    }

    // Wird ausgeführt wenn das Fragment entladen wird
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(thomsLineWordpressArticle: ThomsLineWordpressArticle) {
        val action = HomeFragmentDirections.actionNavHomeToNavThomslineArticleView(thomsLineWordpressArticle.id)
        findNavController().navigate(action)
    }
}