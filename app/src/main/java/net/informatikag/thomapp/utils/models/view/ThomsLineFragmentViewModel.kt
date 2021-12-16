package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.utils.handlers.ThomsLineRecyclerAdapter
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticlePage

/**
 * Saves the Wordpress articles of the ThomsLine fragment
 */
class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    // The articles
    private val _articles = MutableLiveData<ArrayList<ThomsLineWordpressArticlePage>>()
    val articles: LiveData<ArrayList<ThomsLineWordpressArticlePage>> = _articles

    // The last page of the articles
    var lastPage: Int = -1

    // Sets a Page
    fun setArticlePage(id: Int, content:ThomsLineWordpressArticlePage):Boolean{
        var changed = false
        //Check If actually something Changed
        if (_articles.value == null || id >= _articles.value!!.size || (id < _articles.value!!.size && !_articles.value!!.get(id).equals(content))) {
            //If there were no articles previously
            if (_articles.value == null) _articles.value = arrayListOf(content)
            //if the Page is completly new
            else if (id >= _articles.value!!.size) _articles.value?.add(content)
            //If it's just a old Page updating
            else if (id < _articles.value!!.size) _articles.value?.set(id, content)
            changed = true
        }
        _articles.postValue(_articles.value)
        return changed
    }

    // Removes pages after the indexed Page
    fun removeArticlePagesFromIndex(index:Int, recyclerAdapter: ThomsLineRecyclerAdapter){
        val previousSize:Int = recyclerAdapter.itemCount
        recyclerAdapter.notifyItemRangeRemoved(index, (previousSize-index))
        if (articles.value != null) {
            while (_articles.value!!.size > index) {
                _articles.value!!.removeLast()
            }
        }
    }
}