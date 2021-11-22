package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.utils.handlers.ThomsLineRecyclerAdapter
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle
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
        if (_articles.value == null) _articles.value = ArrayList(0)
    fun setArticlePage(id: Int, content:ThomsLineWordpressArticlePage){

        if (id >= _articles.value!!.size) _articles.value?.add(content)
        else if (id < _articles.value!!.size) _articles.value?.set(id, content)

        _articles.postValue(_articles.value)
    }

    // Removes pages after the indexed Page
    fun removeArticlePagesFromIndex(index:Int){
        if (articles.value != null) while (_articles.value!!.size > index) _articles.value!!.removeLast()
    }
}