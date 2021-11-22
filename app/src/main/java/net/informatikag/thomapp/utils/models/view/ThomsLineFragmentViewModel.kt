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
    fun setArticlePage(id: Int, content:ThomsLineWordpressArticlePage, recyclerAdapter: ThomsLineRecyclerAdapter){

        //Check If actually something Changed
        if (_articles.value == null || id >= _articles.value!!.size || (id < _articles.value!!.size && !_articles.value!!.get(id).equals(content))) {
            //If there were no articles previously
            if (_articles.value == null) _articles.value = arrayListOf(content)
            //if the Page is completly new
            else if (id >= _articles.value!!.size) _articles.value?.add(content)
            //If it's just a old Page updating
            else if (id < _articles.value!!.size) _articles.value?.set(id, content)

            // Save the new Values
            _articles.postValue(_articles.value)

            //Update Recycler View
            recyclerAdapter.notifyItemChanged(id)
        }

        _articles.value = _articles.value
    }

    // Removes pages after the indexed Page
    fun removeArticlePagesFromIndex(index:Int, recyclerAdapter: ThomsLineRecyclerAdapter){
        if (articles.value != null) while (_articles.value!!.size > index + 1) {
            _articles.value!!.removeLast()
            recyclerAdapter.notifyItemChanged(_articles.value?.size!!.minus(1))
        }
    }
}