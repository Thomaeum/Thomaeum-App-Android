package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val _articles = MutableLiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>> = _articles

    var lastPage: Int = -1

    fun setArticles(pArticleThomsLines: ArrayList<ArrayList<ThomsLineWordpressArticle>>){
        _articles.value = pArticleThomsLines
    }
}