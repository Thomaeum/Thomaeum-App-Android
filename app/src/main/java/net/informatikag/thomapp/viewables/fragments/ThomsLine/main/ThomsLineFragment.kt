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
import net.informatikag.thomapp.utils.models.view.ThomsLineFragmentViewModel
import java.util.*
import kotlin.collections.ArrayList

class ThomsLineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var viewModel: ThomsLineFragmentViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerAdapter: ThomsLineRecyclerAdapter
    private var _binding: ThomslineMainFragmentBinding? = null
    private var requestsPending: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        if (viewModel.articles.value == null) swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
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

    override fun onRefresh() {
        loadArticles(0)
    }

    fun onItemClick(thomsLineWordpressArticle: ThomsLineWordpressArticle) {
        val action = ThomsLineFragmentDirections.actionNavThomslineToNavThomslineArticleView(thomsLineWordpressArticle.id)
        findNavController().navigate(action)
    }

    fun loadArticles(page:Int){
        viewModel.removeArticlePagesFromIndex(page)

        val requestQueue = Volley.newRequestQueue(this.context)

        for (i in 0 until page+1) {
            reloadPage(i, requestQueue)
        }
    }

    fun isLoading():Boolean {
        return this.requestsPending != 0
    }

    fun reloadPage(id:Int){
        reloadPage(id, Volley.newRequestQueue(this.context))
    }

    fun reloadPage(id: Int, requestQueue:RequestQueue) {
        Log.d("ThomsLine", "Requesting Data for page $id")
        this.requestsPending++
        requestQueue.add(JsonArrayRequest("https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed=wp:featuredmedia&_fields=id,title.rendered, excerpt.rendered, _links, _embedded&&page=${id+1}",
            { response ->
                Log.d("ThomsLine", "Got Data for page $id")

                val data = ArrayList<ThomsLineWordpressArticle>()

                val pages: ArrayList<ArrayList<ThomsLineWordpressArticle>> = if (viewModel.articles.value != null) viewModel.articles.value!! else ArrayList()

                for (j in 0 until response.length()) data.add(ThomsLineWordpressArticle(response.getJSONObject(j), true))

                if (id == pages.size) pages.add(data)
                else if (id < pages.size) pages.set(id, data)

                this.requestsPending--

                viewModel.setArticlePage(id, data)
                recyclerAdapter.notifyItemChanged(id)
            },
            { volleyError ->
                Log.d("ThomsLine", "Request Error while loading Data for page $id")
                if (volleyError.networkResponse?.statusCode == 400){
                    viewModel.lastPage
                    viewModel.lastPage = if(id-1<viewModel.lastPage) viewModel.lastPage else id-1
                    Log.d("ThomsLine", "Page does not exist (last page: ${viewModel.lastPage})")
                } else {
                    Log.d("ThomsLine", "Request failed: ${volleyError.message.toString()}")
                    Snackbar.make(requireActivity().findViewById(R.id.app_bar_main), ThomsLineWordpressArticle.getVolleyError(volleyError, requireActivity()), Snackbar.LENGTH_LONG).show()
                }

                recyclerAdapter.notifyItemChanged(id)
            }
        ))
    }
}