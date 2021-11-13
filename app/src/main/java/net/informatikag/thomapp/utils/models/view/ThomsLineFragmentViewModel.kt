package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.*
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

/**
 * Speichert die WordpressArtikel des ThomsLine Fragments
 */
class ThomsLineFragmentViewModel(application: Application): AndroidViewModel(application) {

    //Die Artikel
    private val _articles = MutableLiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>>()
    val articles: LiveData<ArrayList<ArrayList<ThomsLineWordpressArticle>>> = _articles

    //Die Letze Seite der Artikel,
    var lastPage: Int = -1

    //Sets a Page
    fun setArticlePage(id: Int, content:ArrayList<ThomsLineWordpressArticle>){
        if (_articles.value == null) _articles.value = ArrayList(0)

        if (id >= _articles.value!!.size) _articles.value?.add(content)
        else if (id < _articles.value!!.size) _articles.value?.set(id, content)

        _articles.postValue(_articles.value)
    }

    //Removes pages after the indexed Page
    fun removeArticlePagesFromIndex(index:Int){
        if (articles.value != null) while (_articles.value!!.size > index) _articles.value!!.removeLast()
    }
}