package net.informatikag.thomapp.thomsline.fragments.ArticleList

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.thomsline.utils.WordpressArticle

class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val _articles = MutableLiveData<ArrayList<ArrayList<WordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<WordpressArticle>>> = _articles

    var lastPage: Int = -1

    fun setArticles(pArticles: ArrayList<ArrayList<WordpressArticle>>){
        _articles.value = pArticles
    }
}