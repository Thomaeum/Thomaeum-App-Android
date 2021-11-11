package net.informatikag.thomapp.viewables.fragments.ThomsLine.main

import android.os.Bundle
import android.text.Html
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
    private val defaultImageURL = "https://thoms-line.thomaeum.de/wp-content/uploads/2021/01/Thom-01.jpg"
    private var _binding: ThomslineMainFragmentBinding? = null

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
        recyclerAdapter =  ThomsLineRecyclerAdapter(this)

        //Add Observer to articles to update Recyclerview
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.setPages(it, viewModel.lastPage)
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

        val pages: ArrayList<ArrayList<ThomsLineWordpressArticle>> = if (viewModel.articles.value != null) viewModel.articles.value!! else ArrayList()

        while (pages.size > page) pages.removeLast()

        val requestQueue = Volley.newRequestQueue(this.context)

        for (i in 0 until page+1) {
            Log.d("ThomsLine", "Requesting Data for page $i")
            requestQueue.add(JsonArrayRequest("https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed&&page=${i+1}",
                { response ->
                    val data = ArrayList<ThomsLineWordpressArticle>()

                    for (j in 0 until response.length()) {
                        val current = response.getJSONObject(j)

                        val dateString = current.getString("date").split("[-T:]".toRegex())

                        val article = ThomsLineWordpressArticle(
                            current.getInt("id"),
                            Html.fromHtml(current.getJSONObject("title").getString("rendered"))
                                .toString(),
                            current.getJSONObject("content").getString("rendered"),
                            Html.fromHtml(
                                current.getJSONObject("excerpt").getString("rendered")
                            ).toString(),
                            Array(current.getJSONObject("_embedded").getJSONArray("author").length(), { i -> current.getJSONObject("_embedded").getJSONArray("author").getJSONObject(i).getString("name")}),
                            if (current.getJSONObject("_embedded").has("wp:featuredmedia"))
                                current.getJSONObject("_embedded")
                                    .getJSONArray("wp:featuredmedia")
                                    .getJSONObject(0).getJSONObject("media_details")
                                    .getJSONObject("sizes").getJSONObject("full")
                                    .getString("source_url")
                            else defaultImageURL,
                            Date(
                                dateString[0].toInt(),
                                dateString[1].toInt(),
                                dateString[2].toInt(),
                                dateString[3].toInt(),
                                dateString[4].toInt(),
                                dateString[5].toInt()
                            ),
                            true
                        )
                        data.add(article)
                    }

                    if (i == pages.size) pages.add(data)
                    else if (i < pages.size) pages.set(i, data)

                    if (i == page) viewModel.articles.apply {
                        viewModel.setArticles(pages)
                    }
                },
                { volleyError ->

                    if (volleyError.networkResponse?.statusCode == 400){
                        viewModel.lastPage = if(viewModel.lastPage == -1 || i-1<viewModel.lastPage) i-1 else viewModel.lastPage
                        Log.d("ThomsLine", "Page does not exist")
                    } else {
                        Log.d("ThomsLine", "Request failed: ${volleyError.message.toString()}")
                        Snackbar.make(requireActivity().findViewById(R.id.app_bar_main), ThomsLineWordpressArticle.getVolleyError(volleyError, requireActivity()), Snackbar.LENGTH_LONG).show()
                    }

                    if (i == page) viewModel.articles.apply {
                        viewModel.setArticles(pages)
                    }
                }
            ))
        }
    }
}