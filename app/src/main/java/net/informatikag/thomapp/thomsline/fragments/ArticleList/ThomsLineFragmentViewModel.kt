package net.informatikag.thomapp.thomsline.fragments.ArticleList

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.thomsline.utils.WordpressArticle

class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val defaultImageURL = "https://thoms-line.thomaeum.de/wp-content/uploads/2021/01/Thom-01.jpg"

    private val _articles = MutableLiveData<ArrayList<ArrayList<WordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<WordpressArticle>>> = _articles

    var lastPage: Int = -1

    fun setArticles(pArticles: ArrayList<ArrayList<WordpressArticle>>){
        _articles.apply {
            value = pArticles
        }
    }
}