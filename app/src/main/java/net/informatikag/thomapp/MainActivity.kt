package net.informatikag.thomapp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.volley.*
import net.informatikag.thomapp.databinding.ActivityMainBinding
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * This was mostly auto generated by Android Studio
 */
class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {

        /**
         * Gets a displayable Error String from a VolleyError
         * @param error the Error to generate from
         * @param activity needed to get some Conenctivity Stats
         */
        fun getVolleyError(error: VolleyError?, activity: Activity): String {
            var errorMsg = ""
            if (error == null){
                errorMsg = activity.getString(R.string.network_error_weired_response)
            } else if (error is NoConnectionError) {
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

        // Substitution plan
        val SUBSTITUTION_PLAN_PREVIEW_AMOUNT = 10
        val SUBSTITUTION_PLAN_BASE_URL = "http://isiko404.codes:4000"   //TODO switch to HTTPS

        //Thomsline
        val THOMSLINE_BASE_URL = "https://thoms-line.thomaeum.de/"

        //Thomaeum News
        val THOMAEUM_BASE_URL = "https://thomaeum.de/"

        //WORDPRESS
        val ARTICLES_PER_PAGE: Int = 10
        val WORDPRESS_BASE_URL_LITE: String = "wp-json/wp/v2/posts?_embed=wp:featuredmedia&_fields=id,title.rendered, excerpt.rendered, _links, _embedded"
        val WORDPRESS_BASE_URL_FULL: String = "wp-json/wp/v2/posts?_embed"
        val THOMSLINE_LIST_ARTICLE_PADDING: Int = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Adding SplashScreen
        val splashScreen = installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_thomsline,
                R.id.nav_thomaeum,
                R.id.nav_substitution,
                R.id.nav_substitution_legacy,
                R.id.nav_preferences
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}