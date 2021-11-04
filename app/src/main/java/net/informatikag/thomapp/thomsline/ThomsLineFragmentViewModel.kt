package net.informatikag.thomapp.thomsline

import android.app.Application
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.internal.ContextUtils.getActivity
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

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