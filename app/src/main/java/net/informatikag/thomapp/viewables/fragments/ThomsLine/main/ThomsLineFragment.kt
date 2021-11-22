package net.informatikag.thomapp.viewables.fragments.ThomsLine.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomslineMainFragmentBinding
import net.informatikag.thomapp.utils.handlers.ThomsLineRecyclerAdapter
import net.informatikag.thomapp.utils.ArticleListSpacingDecoration
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticlePage
import net.informatikag.thomapp.utils.models.view.ThomsLineFragmentViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * Pulls a list of articles from the JSON API of the Wordpress instance of the ThomsLine student newspaper.
 * The articles are dynamically loaded with a RecyclerView.
 */
class ThomsLineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var viewModel: ThomsLineFragmentViewModel      // Das Viewmodel in dem die wichtigen Daten des Fragments gespeichert werden
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout     // wird benutz um die Artikel neu zu laden
    private lateinit var recyclerAdapter: ThomsLineRecyclerAdapter  // Hier werden die Artikel angezeigt
    private var _binding: ThomslineMainFragmentBinding? = null      // Verweis zum Layout
    private var requestsPending: Int = 0                            // Anzahl der immoment noch nicht beantworteten Netzwerk Requests

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
        viewModel = ViewModelProvider(this).get(ThomsLineFragmentViewModel::class.java)
        recyclerAdapter =  ThomsLineRecyclerAdapter(this, viewModel)

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

        swipeRefreshLayout.post {
            // Display Refresh Indicator
            swipeRefreshLayout.isRefreshing = true
            // Load First Article Page
            loadArticles(0)
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
        loadArticles(0)
    }

    /**
     * Called when a Article is clicked
     */
    fun onItemClick(thomsLineWordpressArticle: ThomsLineWordpressArticle) {
        val action = ThomsLineFragmentDirections.actionNavThomslineToNavThomslineArticleView(thomsLineWordpressArticle.id)
        findNavController().navigate(action)
    }

    /**
     * Loads all Article pages until "page" and removes all cached pages after it
     */
    fun loadArticles(page:Int){

        // Remove all cached pages after the given one
        viewModel.removeArticlePagesFromIndex(page, recyclerAdapter)

        // Create a new Request Queue
        val requestQueue = Volley.newRequestQueue(this.context)

        // Add requests to load the Pages to the requestQueue
        for (i in 0 until page+1) {
            reloadPage(i, requestQueue)
        }
    }

    // Checks if there are pending Requests at the moment
    fun isLoading():Boolean = this.requestsPending != 0

    // Reload a page without a given Request Queue
    fun reloadPage(id:Int){
        reloadPage(id, Volley.newRequestQueue(this.context))
    }

    // Reload a Page while adding the Requests to a given Request Queue
    fun reloadPage(id: Int, requestQueue:RequestQueue) {
        Log.d("ThomsLine", "Requesting Data for page $id")

        // Add one pending request
        this.requestsPending++

        // Start the Request
        requestQueue.add(JsonArrayRequest("https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed=wp:featuredmedia&_fields=id,title.rendered, excerpt.rendered, _links, _embedded&&page=${id+1}",
            { response ->
                Log.d("ThomsLine", "Got Data for page $id")

                // A Variable to load the Articles to
                val data = ArrayList<ThomsLineWordpressArticle>()

                // Load the Articles from the JSON
                for (j in 0 until response.length()) data.add(ThomsLineWordpressArticle(response.getJSONObject(j), true))

                // Remove one pending Request
                this.requestsPending--

                // Update the RecyclerView
                viewModel.setArticlePage(id, ThomsLineWordpressArticlePage(data.toTypedArray()), recyclerAdapter)
            },
            { volleyError ->
                Log.d("ThomsLine", "Request Error while loading Data for page $id")

                // Check if the Error is caused because loading a non Existing Page
                if (volleyError.networkResponse?.statusCode == 400){

                    // Update the Last Page Variable
                    viewModel.lastPage = if(id-1<viewModel.lastPage) viewModel.lastPage else id-1
                    Log.d("ThomsLine", "Page does not exist (last page: ${viewModel.lastPage})")
                } else {
                    Log.d("ThomsLine", "Request failed: ${volleyError.message.toString()}")
                    // Display a Snackbar, stating the Error
                    Snackbar.make(requireActivity().findViewById(R.id.app_bar_main), ThomsLineWordpressArticle.getVolleyError(volleyError, requireActivity()), Snackbar.LENGTH_LONG).show()
                }

                //recyclerAdapter.notifyItemChanged(id)
            }
        ))
    }
}