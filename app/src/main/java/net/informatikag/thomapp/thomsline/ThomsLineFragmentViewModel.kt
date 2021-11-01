package net.informatikag.thomapp.thomsline

import android.app.Application
import android.content.Context
import android.text.Html
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val defaultImageURL = "https://thoms-line.thomaeum.de/wp-content/uploads/2021/01/Thom-01.jpg"

    private val _articles = MutableLiveData<ArrayList<ArrayList<WordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<WordpressArticle>>> = _articles

    fun loadArticles(page:Int){

        val pages: ArrayList<ArrayList<WordpressArticle>> = if (_articles.value != null) _articles.value!! else ArrayList()

        while (pages.size > page) pages.removeLast()

        val requestQueue = Volley.newRequestQueue(getApplication<Application>().applicationContext)

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

                    if (i == page) _articles.apply {
                        value = pages
                    }
                },
                { volleyError ->
                    Log.d("ThomsLine", "Request failed")
                    Log.d("ThomsLine", volleyError.message.toString())
                }
            ))
        }
    }
}