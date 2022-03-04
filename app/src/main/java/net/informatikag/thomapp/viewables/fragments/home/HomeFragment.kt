package net.informatikag.thomapp.viewables.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.databinding.HomeFragmentBinding
import net.informatikag.thomapp.utils.handlers.HomeListAdapter
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticle
import net.informatikag.thomapp.utils.models.view.ThomaeumViewModel
import net.informatikag.thomapp.utils.models.view.ThomsLineViewModel
import net.informatikag.thomapp.utils.models.view.VertretungsplanViewModel
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder

/**
 * No idea what will be displayed here, currently there is not really something here
 */
class HomeFragment : Fragment(), ArticleClickHandler{

    private var _binding: HomeFragmentBinding? = null                                       // Binding um auf das Layout zuzugreifen
    private val thomsLineViewModel: ThomsLineViewModel by activityViewModels()              // Viewmodel um auf die Artikel der ThomsLine zuzugreifen
    private val rundbriefViewModel: ThomaeumViewModel by activityViewModels()               // Das Viewmodel in dem die wichtigen Daten des Fragments gespeichert werden
    private val vertretungsplanViewModel:VertretungsplanViewModel by activityViewModels()   // Viewmodel für den Vertretunsplan

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)  //Layout aufbauen

        //Vertretungsplan
        val listView = binding.homeVertretungsplanPreview
        val listViewAdapter = HomeListAdapter(this.requireContext(), vertretungsplanViewModel)
        vertretungsplanViewModel.entrys.observe(viewLifecycleOwner, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listView.adapter = listViewAdapter
        if(vertretungsplanViewModel.isEmpty()) vertretungsplanViewModel.loadVertretunsplan()

        // ThomsLine
        val thomslineArticleViewHolder = ThomsLineArticleViewHolder(if(thomsLineViewModel.isEmpty()) 0 else 1,  binding.homeArticlePreviewThomsline.root,this, true)
        if (thomsLineViewModel.isEmpty())
            Volley.newRequestQueue(this.context).add(JsonArrayRequest(MainActivity.THOMSLINE_BASE_URL + MainActivity.WORDPRESS_BASE_URL_LITE + "&&page=1&&per_page=1",
                { response ->
                    thomslineArticleViewHolder.bind(WordpressArticle(response.getJSONObject(0), true, thomsLineViewModel.BASE_URL), this)
                },
                { volleyError ->
                    thomslineArticleViewHolder.loadingState = -1
                }
            ))
        else {
            thomslineArticleViewHolder.bind(thomsLineViewModel.articles.value!![0].articles[0], this)
        }

        // Rundbrief
        val rundbriefArticleViewHolder = ThomsLineArticleViewHolder(if(rundbriefViewModel.isEmpty()) 0 else 1,  binding.homeArticlePreviewNews.root,this, true)
        if (rundbriefViewModel.isEmpty())
            Volley.newRequestQueue(this.context).add(JsonArrayRequest(MainActivity.THOMAEUM_BASE_URL + MainActivity.WORDPRESS_BASE_URL_LITE + "&&page=1&&per_page=1",
                { response ->
                    rundbriefArticleViewHolder.bind(WordpressArticle(response.getJSONObject(0), true, rundbriefViewModel.BASE_URL), this)
                },
                { volleyError ->
                    rundbriefArticleViewHolder.loadingState = -1
                }
            ))
        else {
            rundbriefArticleViewHolder.bind(rundbriefViewModel.articles.value!![0].articles[0], this)
        }

        return binding.root
    }

    // Wird ausgeführt wenn das Fragment entladen wird
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(wordpressArticle: WordpressArticle) {
        if(wordpressArticle.base_url.equals(MainActivity.THOMSLINE_BASE_URL))
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavThomslineArticleView(wordpressArticle.id))
        else if(wordpressArticle.base_url.equals(MainActivity.THOMAEUM_BASE_URL))
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToThomaeumArticleFragment(wordpressArticle.id))
    }
}