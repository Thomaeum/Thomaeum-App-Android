package net.informatikag.thomapp.viewables.fragments.ThomsLine.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomslineMainFragmentBinding
import net.informatikag.thomapp.utils.handlers.WordpressRecyclerAdapter
import net.informatikag.thomapp.utils.ArticleListSpacingDecoration
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticle
import net.informatikag.thomapp.utils.models.data.WordpressPage
import net.informatikag.thomapp.utils.models.view.ThomsLineViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * Pulls a list of articles from the JSON API of the Wordpress instance of the ThomsLine student newspaper.
 * The articles are dynamically loaded with a RecyclerView.
 */
class ThomsLineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, ArticleClickHandler {

    private var _binding: ThomslineMainFragmentBinding? = null                  // Verweis zum Layout
    private val viewModel: ThomsLineViewModel by activityViewModels()   // Das Viewmodel in dem die wichtigen Daten des Fragments gespeichert werden
    private lateinit var recyclerAdapter: WordpressRecyclerAdapter              // Hier werden die Artikel angezeigt
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout                 // wird benutz um die Artikel neu zu laden

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Will be executed when the fragment is opened
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = ThomslineMainFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Instantiate Variables
        recyclerAdapter =  WordpressRecyclerAdapter(this, viewModel)

        //Add Observer to articles to update Recyclerview
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = false
        })

        //region Init SwipeRefresh Layout
        swipeRefreshLayout = root.findViewById(R.id.thomsline_swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            R.color.secondaryColor
        )

        if(viewModel.isEmpty()) {
            swipeRefreshLayout.post {
                // Display Refresh Indicator
                swipeRefreshLayout.isRefreshing = true
                // Load First Article Page
                viewModel.loadArticles(0, true, requireContext(), recyclerAdapter)
            }
        }
        //endregion

        //region Init Recycler View
        _binding?.thomslineRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ThomsLineFragment.context)
            addItemDecoration(ArticleListSpacingDecoration())
            adapter = recyclerAdapter
        }
        //endregion

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when the SwipeRefresh Layout is triggerd
     */
    override fun onRefresh() {

    /**
     * Loads all Article pages until "page" and removes all cached pages after it
     */
    fun loadArticles(page:Int, reloadAll: Boolean){
        // Remove all cached pages after the given one
        if(page == 0) {
            viewModel.removeArticlePagesFromIndex(1, recyclerAdapter)
            viewModel.lastPage = -1
        }

        // Create a new Request Queue
        val requestQueue = Volley.newRequestQueue(this.context)

        // Add requests to load the Pages to the requestQueue
        if(reloadAll)
            for (i in 0 until page+1) {
                viewModel.reloadPage(i, requestQueue, recyclerAdapter)
            }
        else viewModel.reloadPage(page, recyclerAdapter)
    }

    /**
     * Called when a Article is clicked
     */
    override fun onItemClick(wordpressArticle: WordpressArticle) {
        val action = ThomsLineFragmentDirections.actionNavThomslineToNavThomslineArticleView(wordpressArticle.id)
        findNavController().navigate(action)
    }
}