package net.informatikag.thomapp.utils.models.view

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.handlers.WordpressRecyclerAdapter
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticleData
import net.informatikag.thomapp.utils.models.data.WordpressPageData
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder

/**
 * Saves the Wordpress articles of the ThomsLine fragment
 */
abstract class WordpressViewModel(application: Application): AndroidViewModel(application) {

    // The articles
    private val _articles = MutableLiveData<ArrayList<WordpressPageData>>()
    val articles: LiveData<ArrayList<WordpressPageData>> = _articles

    abstract val BASE_URL:String

    // The last page of the articles
    var lastPage: Int = -1

    //Checks if there are Articles
    fun isEmpty():Boolean = _articles.value == null || _articles.value?.size == 0

    // Sets a Page
    fun postArticlePage(id: Int, content:WordpressPageData, recyclerAdapter: WordpressRecyclerAdapter){
        val changed = isEmpty() || id >= _articles.value!!.size || (id < _articles.value!!.size && !_articles.value!!.get(id).equals(content))
        //Check If actually something Changed
        if (changed) {
            //If there were no articles previously
            val positionStart = id * MainActivity.ARTICLES_PER_PAGE
            if (_articles.value == null) {
                _articles.value = arrayListOf(content)
                recyclerAdapter.notifyItemRangeInserted(positionStart, content.articleData.size);
            }
            //if the Page is completly new
            else if (id >= _articles.value!!.size) {
                _articles.value?.add(content)
                recyclerAdapter.notifyItemRangeInserted(positionStart, content.articleData.size);
            }
            //If it's just a old Page updating
            else if (id < _articles.value!!.size) {
                _articles.value?.set(id, content)
                recyclerAdapter.notifyItemRangeChanged(positionStart, content.articleData.size);
            }
        }
        _articles.postValue(_articles.value)
    }

    // Removes pages after the indexed Page
    fun removeArticlePagesFromIndex(index:Int, recyclerAdapter: WordpressRecyclerAdapter){
        val previousSize:Int = recyclerAdapter.itemCount
        recyclerAdapter.notifyItemRangeRemoved(index*MainActivity.ARTICLES_PER_PAGE, (previousSize-index))
        if (!isEmpty()) {
            while (_articles.value!!.size > index) {
                _articles.value!!.removeLast()
            }
        }
    }

    fun getByID(id:Int):WordpressArticleData?{
        if (!isEmpty()) {
            for (p in 0.._articles.value!!.size) {
                val tempReturn = _articles.value!![p].getByID(id)
                if (tempReturn != null) return tempReturn
            }
        }
        return null
    }

    // Reload a page without a given Request Queue
    fun reloadPage(id:Int, recyclerAdapter:WordpressRecyclerAdapter){
        reloadPage(id, Volley.newRequestQueue(recyclerAdapter.fragment.context), recyclerAdapter)
    }

    fun reloadPage(id:Int, requestQueue: RequestQueue, recyclerAdapter:WordpressRecyclerAdapter) {
        Log.d("ThomsLine", "Requesting Data for page $id")

        // Start the Request
        requestQueue.add(JsonArrayRequest(BASE_URL + MainActivity.WORDPRESS_BASE_URL_LITE + "&&page=${id+1}",
            { response ->
                Log.d("ThomsLine", "Got Data for page $id")

                // A Variable to load the Articles to
                val data = ArrayList<WordpressArticleData>()

                // Load the Articles from the JSON
                for (j in 0 until response.length()) data.add(WordpressArticleData(response.getJSONObject(j), true, BASE_URL))

                // Update the RecyclerView
                postArticlePage(id, WordpressPageData(data.toTypedArray()), recyclerAdapter)

            },
            { volleyError ->
                Log.d("ThomsLine", "Request Error while loading Data for page $id")

                // Check if the Error is caused because loading a non Existing Page
                if (volleyError.networkResponse?.statusCode == 400){

                    // Update the Last Page Variable
                    lastPage = if(id-1<lastPage) lastPage else id-1
                    recyclerAdapter.notifyItemChanged(recyclerAdapter.itemCount-1)
                    Log.d("ThomsLine", "Page does not exist (last page: $lastPage)")
                } else {
                    Log.d("ThomsLine", "Request failed: ${volleyError.message.toString()}")
                    // Display a Snackbar, stating the Error
                    Snackbar.make(recyclerAdapter.fragment.requireActivity().findViewById(R.id.app_bar_main), WordpressArticleData.getVolleyError(volleyError, recyclerAdapter. fragment.requireActivity()), Snackbar.LENGTH_LONG).show()
                }
            }
        ))
    }

    /**
     * Loads all Article pages until "page" and removes all cached pages after it
     */
    fun loadArticles(page:Int, reloadAll: Boolean, context: Context, recyclerAdapter:WordpressRecyclerAdapter){
        // Remove all cached pages after the given one
        if(page == 0) {
            removeArticlePagesFromIndex(1, recyclerAdapter)
            lastPage = -1
        }

        // Create a new Request Queue
        val requestQueue = Volley.newRequestQueue(context)

        // Add requests to load the Pages to the requestQueue
        if(reloadAll)
            for (i in 0 until page+1) {
                reloadPage(i, requestQueue, recyclerAdapter)
            }
        else reloadPage(page, recyclerAdapter)
    }

    fun loadFirstArticleToViewHolder(thomslineArticleViewHolder: ThomsLineArticleViewHolder, context: Context, clickHandler: ArticleClickHandler) {
        if (this.isEmpty())
            Volley.newRequestQueue(context)
                .add(JsonArrayRequest(BASE_URL + MainActivity.WORDPRESS_BASE_URL_LITE + "&&page=1&&per_page=1",
                    { response ->
                        thomslineArticleViewHolder.bind(
                            WordpressArticleData(
                                response.getJSONObject(0),
                                true,
                                BASE_URL
                            ), clickHandler
                        )
                    },
                    { volleyError ->
                        thomslineArticleViewHolder.loadingState = -1
                    }
                ))
        else {
            thomslineArticleViewHolder.bind(
                articles.value!![0].articleData[0],
                clickHandler
            )
        }
    }
}