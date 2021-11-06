package net.informatikag.thomapp.thomsline.fragments.ArticleList

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import net.informatikag.thomapp.thomsline.fragments.ArticleList.RecyclerView.ThomslineRecyclerAdapter
import net.informatikag.thomapp.thomsline.fragments.ArticleList.RecyclerView.TopSpacingItemDecoration
import net.informatikag.thomapp.thomsline.utils.WordpressArticle
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

class ThomsLineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var viewModel: ThomsLineFragmentViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerAdapter: ThomslineRecyclerAdapter
    private val defaultImageURL = "https://thoms-line.thomaeum.de/wp-content/uploads/2021/01/Thom-01.jpg"
    private var _binding: FragmentThomslineBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThomslineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Instantiate Variables
        viewModel = ViewModelProvider(this).get(ThomsLineFragmentViewModel::class.java)
        recyclerAdapter =  ThomslineRecyclerAdapter(this)

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
            addItemDecoration(TopSpacingItemDecoration())
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


    fun onItemClick(wordpressArticle: WordpressArticle) {
        val action =
            ThomsLineFragmentDirections.actionNavThomslineToNavThomslineArticleView(
                wordpressArticle.title,
                wordpressArticle.imageURL,
                wordpressArticle.content
            )
        findNavController().navigate(action)
    }

    fun loadArticles(page:Int){

        val pages: ArrayList<ArrayList<WordpressArticle>> = if (viewModel.articles.value != null) viewModel.articles.value!! else ArrayList()

        while (pages.size > page) pages.removeLast()

        val requestQueue = Volley.newRequestQueue(this.context)

        for (i in 0 until page+1) {
            Log.d("ThomsLine", "Requesting Data for page $i")
            requestQueue.add(JsonArrayRequest("https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed&&page=${i+1}",
                { response ->
                    val data = ArrayList<WordpressArticle>()

                    for (j in 0 until response.length()) {
                        val current = response.getJSONObject(j)
                        var article = WordpressArticle(
                            current.getInt("id"),
                            Html.fromHtml(current.getJSONObject("title").getString("rendered"))
                                .toString(),
                            current.getJSONObject("content").getString("rendered"),
                            Html.fromHtml(
                                current.getJSONObject("excerpt").getString("rendered")
                            ).toString(),
                            current.getJSONObject("_embedded").getJSONArray("author")
                                .getJSONObject(0).getString("name"),
                            if (current.getJSONObject("_embedded").has("wp:featuredmedia"))
                                current.getJSONObject("_embedded")
                                    .getJSONArray("wp:featuredmedia")
                                    .getJSONObject(0).getJSONObject("media_details")
                                    .getJSONObject("sizes").getJSONObject("full")
                                    .getString("source_url")
                            else defaultImageURL
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
                        Snackbar.make(requireActivity().findViewById(R.id.app_bar_main), requireActivity().getVolleyError(volleyError), Snackbar.LENGTH_LONG).show()
                    }

                    if (i == page) viewModel.articles.apply {
                        viewModel.setArticles(pages)
                    }
                }
            ))
        }
    }

    fun Activity.getVolleyError(error: VolleyError): String {
        var errorMsg = ""
        if (error is NoConnectionError) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = cm.activeNetworkInfo
            errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                getString(R.string.network_error_server_error)
            } else {
                getString(R.string.network_error_not_connected)
            }
        } else if (error is NetworkError || error.cause is ConnectException) {
            errorMsg = getString(R.string.network_error_not_connected)
        } else if (error.cause is MalformedURLException) {
            errorMsg = getString(R.string.network_error_weired_response)
        } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
            errorMsg = getString(R.string.network_error_weired_response)
        } else if (error.cause is OutOfMemoryError) {
            errorMsg = getString(R.string.network_error_out_of_memory)
        } else if (error is AuthFailureError) {
            errorMsg = getString(R.string.network_error_generic)
        } else if (error is ServerError || error.cause is ServerError) {
            getString(R.string.network_error_server_error)
        } else if (
            error is TimeoutError ||
            error.cause is SocketTimeoutException ||
            error.cause is ConnectTimeoutException ||
            error.cause is SocketException ||
            (error.cause!!.message != null && error.cause!!.message!!.contains("Your connection has timed out, please try again"))
        ) {
            errorMsg = getString(R.string.network_error_timeout)
        } else {
            errorMsg = getString(R.string.network_error_generic)
        }
        return errorMsg
    }
}