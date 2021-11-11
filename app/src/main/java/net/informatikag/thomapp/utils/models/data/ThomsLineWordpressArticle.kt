package net.informatikag.thomapp.utils.models.data

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Html
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import net.informatikag.thomapp.R
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.*

data class ThomsLineWordpressArticle(
    var id: Int,
    var title: String?,
    var imageURL: String?,
    var excerpt: String?,
    var content: String?,
    var authors: Array<String>?,
    var date: Date?,
    var loaded:Boolean,
    var liteVersion:Boolean
) {
    val LITE_URL = ""
    val FULL_URL = "https://thoms-line.thomaeum.de/wp-json/wp/v2/posts?_embed"

    constructor(id:Int,
                context: Context,
                callback: (ThomsLineWordpressArticle, VolleyError?) -> Unit
    ):this(
        id,
        null,
        null,
        null,
        null,
        null,
        null,
        false,
        false
    ){ refresh(context, callback) }

    constructor(json: JSONObject, liteVersion: Boolean): this(
        getIDFromJSON(json),
        getTitleFromJSON(json),
        getImageURLFromJSON(json),
        getExcerptFromJSON(json),
        if (!liteVersion) getContentFromJSON(json) else null,
        if (!liteVersion) getAuthorsFromJSON(json) else null,
        if (!liteVersion) getDateFromJSON(json) else null,
        true,
        liteVersion
    )

    fun refresh(
        context: Context,
        callback: (ThomsLineWordpressArticle, VolleyError?) -> Unit
    ) {
        val url = (if (this.liteVersion) LITE_URL else FULL_URL) + "&include=$id"
        Volley.newRequestQueue(context).add(
            JsonArrayRequest(url,
                { response ->
                    val json = response.getJSONObject(0)
                    this.title = getTitleFromJSON(json)
                    this.imageURL = getImageURLFromJSON(json)
                    this.excerpt = getExcerptFromJSON(json)

                    if(!this.liteVersion){
                        this.content = getContentFromJSON(json)
                        this.authors = getAuthorsFromJSON(json)
                        this.date = getDateFromJSON(json)
                    }

                    this.loaded = false
                    callback(this, null)
                },
                { volleyError ->
                    this.title = null
                    this.content = null
                    this.excerpt = null
                    this.authors = null
                    this.imageURL = null
                    this.date = null
                    this.loaded = false
                    callback(this, volleyError)
                }
            )
        )
    }

    fun getAuthorString():String{
        var output = ""
        if (authors != null) {
            for (i in 0 until authors!!.size) {
                output += authors!![i]
                output += if (i+2 == authors!!.size) " und " else if (i+1 == authors!!.size) "" else ", "
                output += when(i) {
                    authors!!.size-2 -> " und "
                    authors!!.size-1 -> ""
                    else -> ", "
                }
            }
        }
        return output
    }

    companion object {
        fun getIDFromJSON(json: JSONObject):Int = json.getInt("id")
        fun getTitleFromJSON(json: JSONObject):String? = try {
            Html.fromHtml(json.getJSONObject("title").getString("rendered")).toString()
        } catch (e: Exception){null}

        fun getContentFromJSON(json: JSONObject):String? = try {
            json.getJSONObject("content").getString("rendered")
        } catch (e: Exception){null}

        fun getExcerptFromJSON(json: JSONObject):String? = try {
            Html.fromHtml(
                json.getJSONObject("excerpt").getString("rendered")
            ).toString()
        } catch (e: Exception){null}

        fun getAuthorsFromJSON(json: JSONObject):Array<String>? = try {
            Array(
                json.getJSONObject("_embedded").getJSONArray("author").length()
            ) { i ->
                json.getJSONObject("_embedded").getJSONArray("author")
                    .getJSONObject(i).getString("name")
            }
        } catch (e: Exception){null}

        fun getImageURLFromJSON(json: JSONObject):String? = try {
            json.getJSONObject("_embedded")
                .getJSONArray("wp:featuredmedia")
                .getJSONObject(0).getJSONObject("media_details")
                .getJSONObject("sizes").getJSONObject("full")
                .getString("source_url")
        } catch (e: Exception) {null}

        fun getDateFromJSON(json: JSONObject):Date? = try {
            val dateStrings = json.getString("date").split("[-T:]".toRegex())
            Date(
                dateStrings[0].toInt(),
                dateStrings[1].toInt(),
                dateStrings[2].toInt(),
                dateStrings[3].toInt(),
                dateStrings[4].toInt(),
                dateStrings[5].toInt()
            )
        } catch (e: Exception) {null}

        fun getVolleyError(error: VolleyError, activity: Activity): String {
            var errorMsg = ""
            if (error is NoConnectionError) {
                val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activeNetwork: NetworkInfo? = null
                activeNetwork = cm.activeNetworkInfo
                errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                    activity.getString(R.string.network_error_server_error)
                } else {
                    activity.getString(R.string.network_error_not_connected)
                }
            } else if (error is NetworkError || error.cause is ConnectException) {
                errorMsg = activity.getString(R.string.network_error_not_connected)
            } else if (error.cause is MalformedURLException) {
                errorMsg = activity.getString(R.string.network_error_weired_response)
            } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
                errorMsg = activity.getString(R.string.network_error_weired_response)
            } else if (error.cause is OutOfMemoryError) {
                errorMsg = activity.getString(R.string.network_error_out_of_memory)
            } else if (error is AuthFailureError) {
                errorMsg = activity.getString(R.string.network_error_generic)
            } else if (error is ServerError || error.cause is ServerError) {
                activity.getString(R.string.network_error_server_error)
            } else if (
                error is TimeoutError ||
                error.cause is SocketTimeoutException ||
                error.cause is ConnectTimeoutException ||
                error.cause is SocketException ||
                (error.cause!!.message != null && error.cause!!.message!!.contains("Your connection has timed out, please try again"))
            ) {
                errorMsg = activity.getString(R.string.network_error_timeout)
            } else {
                errorMsg = activity.getString(R.string.network_error_generic)
            }
            return errorMsg
        }
    }
}
