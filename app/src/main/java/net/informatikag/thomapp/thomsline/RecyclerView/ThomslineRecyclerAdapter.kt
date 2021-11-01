package net.informatikag.thomapp.thomsline.RecyclerView

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.WordpressArticle

class ThomslineRecyclerAdapter(
    swipeRefreshLayout: SwipeRefreshLayout,
    val itemClickListener: ItemClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var swipeRefreshLayout: SwipeRefreshLayout = swipeRefreshLayout
    private var pages: ArrayList<ArrayList<WordpressArticle>> = ArrayList()
    private val perPage:Int = 10
    private val defaultImageURL = "https://thoms-line.thomaeum.de/wp-content/uploads/2021/01/Thom-01.jpg"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_article, parent, false),
                itemClickListener
            )
        }
        return ThomsLineLoadingViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_loading, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ThomsLineArticleViewHolder -> {
                var pageIndex = position/(perPage)
                val itemIndex = position%perPage

                holder.bind(pages
                    .get(pageIndex)
                    .get(itemIndex)
                )
            }
            is ThomsLineLoadingViewholder -> {
                loadArticles(pages.size+1)
            }
        }
    }

    override fun getItemCount(): Int {
        if (pages.size == 0) return 0
        else return (pages.size-1) * perPage + pages[pages.size-1].size + 1
    }

    override fun getItemViewType(position: Int): Int {
//        Log.d("test", "${getItemCount()}")
        if (position == getItemCount()-1 && position != 0) return 1
        else return 0
    }

    fun loadArticles(page:Int){

        while (pages.size > page) pages.removeLast()

        val requestQueue = Volley.newRequestQueue(this.swipeRefreshLayout.context)

        for (i in 0 until page+1) {
            Log.d("ThomsLine", "Requesting Data for page $i")
            requestQueue.add(JsonArrayRequest("https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed&&page=${i+1}",
                { response ->
                    val data = ArrayList<WordpressArticle>()

                    for (j in 0 until response.length()) {
                        val current = response.getJSONObject(j)
                        data.add(
                            WordpressArticle(
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
                        )
                    }

                    if (i == pages.size) pages.add(data)
                    else if (i < pages.size) pages.set(i, data)

                    if (i == page) this.notifyDataSetChanged()

                    swipeRefreshLayout.setRefreshing(false);
                },
                { volleyError ->
                    Log.d("ThomsLine", "Request failed")
                    Log.d("ThomsLine", volleyError.message.toString())
                }
            ))
        }
    }
}