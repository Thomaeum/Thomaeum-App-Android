package net.informatikag.thomapp.utils.models.view

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.handlers.WordpressRecyclerAdapter
import net.informatikag.thomapp.utils.models.data.WordpressArticle
import net.informatikag.thomapp.utils.models.data.WordpressPage

/**
 * Saves the Wordpress articles of the ThomsLine fragment
 */
abstract class WordpressViewModel(application: Application): AndroidViewModel(application) {

    // The articles
    private val _articles = MutableLiveData<ArrayList<WordpressPage>>()
    val articles: LiveData<ArrayList<WordpressPage>> = _articles

    abstract val BASE_URL:String

    // The last page of the articles
    var lastPage: Int = -1

    //Checks if there are Articles
    fun isEmpty():Boolean = _articles.value == null

    // Sets a Page
    fun postArticlePage(id: Int, content:WordpressPage, recyclerAdapter: WordpressRecyclerAdapter){
        val changed = isEmpty() || id >= _articles.value!!.size || (id < _articles.value!!.size && !_articles.value!!.get(id).equals(content))
        //Check If actually something Changed
        if (changed) {
            //If there were no articles previously
            val positionStart = id * MainActivity.ARTICLES_PER_PAGE
            if (_articles.value == null) {
                _articles.value = arrayListOf(content)
                recyclerAdapter.notifyItemRangeInserted(positionStart, content.articles.size);
            }
            //if the Page is completly new
            else if (id >= _articles.value!!.size) {
                _articles.value?.add(content)
                recyclerAdapter.notifyItemRangeInserted(positionStart, content.articles.size);
            }
            //If it's just a old Page updating
            else if (id < _articles.value!!.size) {
                _articles.value?.set(id, content)
                recyclerAdapter.notifyItemRangeChanged(positionStart, content.articles.size);
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

    fun getByID(id:Int):WordpressArticle?{
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
                val data = ArrayList<WordpressArticle>()

                // Load the Articles from the JSON
                for (j in 0 until response.length()) data.add(WordpressArticle(response.getJSONObject(j), true, BASE_URL))

                // Update the RecyclerView
                postArticlePage(id, WordpressPage(data.toTypedArray()), recyclerAdapter)

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
                    Snackbar.make(recyclerAdapter.fragment.requireActivity().findViewById(R.id.app_bar_main), WordpressArticle.getVolleyError(volleyError, recyclerAdapter. fragment.requireActivity()), Snackbar.LENGTH_LONG).show()
                }

                //recyclerAdapter.notifyItemChanged(id)
            }
        ))
    }
}