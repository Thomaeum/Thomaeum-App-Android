package net.informatikag.thomapp.thomsline

import android.graphics.Rect
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import net.informatikag.thomapp.thomsline.models.WordpressArticle

class ThomsLineFragment : Fragment(){

    private lateinit var postAdapter: ThomslineRecyclerAdapter
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

        initRecyclerView()
        loadArticles()

        return root
    }

    private fun loadArticles(){

        val url="https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed"

        Log.d("ThomsLine", "Requesting Data")

        val wordpressRequest = JsonArrayRequest(url,
            { response ->
                Log.d("ThomsLine", "Request Successfull")

                val data = ArrayList<WordpressArticle>()

                for (i in 0 until response.length()){
                    val current = response.getJSONObject(i)
                    data.add(
                        WordpressArticle(
                            current.getInt("id"),
                            Html.fromHtml(current.getJSONObject("title").getString("rendered")).toString(),
                            Html.fromHtml(current.getJSONObject("content").getString("rendered")).toString(),
                            Html.fromHtml(current.getJSONObject("excerpt").getString("rendered")).toString(),
                            current.getJSONObject("_embedded").getJSONArray("author").getJSONObject(0).getString("name"),
                            current.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium").getString("source_url")
                        )
                    )
                }

                Log.d("test", data[0].title)

                postAdapter.submitList(data)
            },
            { volleyError ->
                Log.d("ThomsLine", "Request failed")
                Log.d("ThomsLine", volleyError.message.toString())
            }
        )

        Volley.newRequestQueue(this.context).add(wordpressRequest)

    }

    private fun initRecyclerView(){
        _binding?.thomslineRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ThomsLineFragment.context)
            postAdapter = ThomslineRecyclerAdapter()
            addItemDecoration(TopSpacingItemDecoration(30))
            adapter = postAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TopSpacingItemDecoration(private val padding: Int): RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = padding/2
            outRect.bottom = padding/2
            outRect.left = padding
            outRect.right = padding
        }
    }
}