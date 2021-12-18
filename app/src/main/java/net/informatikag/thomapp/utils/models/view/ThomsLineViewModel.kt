package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.utils.handlers.ThomsLineRecyclerAdapter
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticlePage

/**
 * Saves the Wordpress articles of the ThomsLine fragment
 */
class ThomsLineViewModel(application: Application): AndroidViewModel(application) {

    // The articles
    private val _articles = MutableLiveData<ArrayList<ThomsLineWordpressArticlePage>>()
    val articles: LiveData<ArrayList<ThomsLineWordpressArticlePage>> = _articles

    // The last page of the articles
    var lastPage: Int = -1

    fun isEmpty():Boolean = articles.value == null

    // Sets a Page
    fun setArticlePage(id: Int, content:ThomsLineWordpressArticlePage, recyclerAdapter: ThomsLineRecyclerAdapter){
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
    fun removeArticlePagesFromIndex(index:Int, recyclerAdapter: ThomsLineRecyclerAdapter){
        val previousSize:Int = recyclerAdapter.itemCount
        recyclerAdapter.notifyItemRangeRemoved(index*MainActivity.ARTICLES_PER_PAGE, (previousSize-index))
        if (articles.value != null) {
            while (_articles.value!!.size > index) {
                _articles.value!!.removeLast()
            }
        }
    }
}