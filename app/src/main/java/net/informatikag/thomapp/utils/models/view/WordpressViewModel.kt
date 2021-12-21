package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.utils.handlers.ThomsLineRecyclerAdapter
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

    fun isEmpty():Boolean = _articles.value == null

    // Sets a Page
    fun setArticlePage(id: Int, content:WordpressPage, recyclerAdapter: ThomsLineRecyclerAdapter){
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
}