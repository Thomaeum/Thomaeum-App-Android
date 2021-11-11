package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val _articles = MutableLiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>> = _articles

    var lastPage: Int = -1

    fun setArticlePage(id: Int, content:ArrayList<ThomsLineWordpressArticle>){
        if (_articles.value == null) _articles.value = ArrayList(0)

        if (id >= _articles.value!!.size) _articles.value?.add(content)
        else if (id < _articles.value!!.size) _articles.value?.set(id, content)

        _articles.postValue(_articles.value)
    }

//    fun setLastPage(index: Int) {
//        this.lastPage = index
//    }

    fun removeArticlePagesFromIndex(index:Int){
        if (articles.value != null) while (_articles.value!!.size > index) _articles.value!!.removeLast()
    }
}