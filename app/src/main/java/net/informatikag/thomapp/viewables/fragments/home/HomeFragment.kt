package net.informatikag.thomapp.viewables.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.HomeFragmentBinding
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticlePage
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

        Volley.newRequestQueue(this.context).add(JsonArrayRequest(MainActivity.WORDPRESS_BASE_URL_LITE + "&&page=1&&per_page=1",
            { response ->
                articleViewHolder!!.bind(ThomsLineWordpressArticle(response.getJSONObject(0), true), this)
            },
            { volleyError ->
                //Todo Add Error Version
            }
        ))

//        articleViewHolder!!.bind(post, this);

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