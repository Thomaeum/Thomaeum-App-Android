package net.informatikag.thomapp.viewables.fragments.thomaeum

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.android.volley.VolleyError
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomaeumArticleFragmentBinding
import net.informatikag.thomapp.utils.handlers.DrawableImageGetter
import net.informatikag.thomapp.utils.handlers.WordpressHtmlTagHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticleData
import net.informatikag.thomapp.utils.models.view.ThomaeumViewModel

/**
 * Loads an article from the WordpressAPI and displays title, cover image and content
 */
class ThomaeumArticleFragment : Fragment() {

    private val args: ThomaeumArticleFragmentArgs by navArgs()         // Die Argumente die beim Wechseln zu diesem Fragment übergeben werden
    private var _binding: ThomaeumArticleFragmentBinding? = null       // Binding um das Layout zu erreichen
    private lateinit var articleData: WordpressArticleData                      // das WordpressArticle Object, welches angezeigt wird
    private val viewmodel: ThomaeumViewModel by activityViewModels()   // Das Viewmodel in dem alle Artikel gespeichert sind

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Will be executed when the fragment is opened
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = ThomaeumArticleFragmentBinding.inflate(inflater, container, false)

        // Initiate Article Loading and hide Content Containers
        binding.thomaeumArticleScrollview.visibility = View.GONE
        val possiblePost = viewmodel.getByID(args.id)
        if (possiblePost!=null) {
            articleData = possiblePost
            if(articleData.liteVersion) {
                articleData.refresh(this.requireContext(), false)
                { article, error -> articleRefreshCallback(article, error) }
                binding.thomaeumArticleSwipeRefreshLayout.isRefreshing = true
            } else {
                loadArticleToViews()
            }
        }
        else {
            articleData = WordpressArticleData(args.id, false, viewmodel.BASE_URL, this.requireContext())
            { article, error -> articleRefreshCallback(article, error) }
            binding.thomaeumArticleSwipeRefreshLayout.isRefreshing = true
        }

        // Initiate Article Loading when a Refresh is triggered
        binding.thomaeumArticleSwipeRefreshLayout.setOnRefreshListener {
            TransitionManager.beginDelayedTransition(binding.thomaeumArticleScrollview)
            binding.thomaeumArticleScrollview.visibility = View.INVISIBLE
            articleData.refresh(this.requireContext(),false) { article, error ->
                articleRefreshCallback(
                    article,
                    error
                )
            }
        }
        return binding.root
    }

    /**
     * Is Called when the Article was refreshed
     */
    fun articleRefreshCallback(articleData: WordpressArticleData?, error: VolleyError?){
        if (error == null) {
            // If there were no Errors just load the Article to the atribute and then to the layout
            this.articleData = articleData!!
            loadArticleToViews()
        }
        // If there were Errors, display them in a Snackbar
        else Snackbar.make(
            requireActivity().findViewById(R.id.app_bar_main),
            WordpressArticleData.getVolleyError(error, this.requireActivity()),
            Snackbar.LENGTH_LONG
        ).show()
        binding.thomaeumArticleSwipeRefreshLayout.isRefreshing = false
    }

    /**
     *  Loads a given WordpressArticle to the Layout
     */
    fun loadArticleToViews() {
        // Make shure the Fragment is still Loaded
        if (this.context == null) return
        binding.thomaeumArticleScrollview.visibility = View.VISIBLE

        // Change Title TextView
        binding.thomaeumArticleTitle.text = this.articleData.title
        binding.thomaeumArticleTitle.visibility = View.VISIBLE

        // Load Author
        binding.thomaeumArticleAuthor.text = this.articleData.getAuthorString()
        binding.thomaeumArticleAuthor.visibility = View.VISIBLE

        // Load Title Image
        val imageView: ImageView = binding.thomaeumArticleImage
        if (this.articleData.imageURL != null)
            Picasso.get()
                .load(this.articleData.imageURL)
                .placeholder(R.drawable.ic_wordpress_article_default)
                .into(imageView)
        else imageView.visibility = View.GONE

        // Load Date
        binding.thomaeumArticleDate.text = "${this.articleData.date?.hours}:${this.articleData.date?.minutes} - ${this.articleData.date?.date}.${this.articleData.date?.month}.${this.articleData.date?.year}"
        binding.thomaeumArticleDate.visibility = View.VISIBLE

        // Load Content
        val contentView: TextView = binding.thomaeumArticleContent
        var content = this.articleData.content

        // Remove multiple Whitespaces
        content = content?.replace("(\\s|&nbsp;)+".toRegex(), " ")

        // Add Linebreak bevore Caption
        content = content?.replace("<figcaption>", "<br><figcaption>", true)

        // Load HTML to Textview
        contentView.setText(
            Html.fromHtml(
                content,
                Html.FROM_HTML_MODE_LEGACY,
                DrawableImageGetter(resources, contentView, null, null),
                WordpressHtmlTagHandler()
            )
        )

        // Make the Article visible
        binding.thomaeumArticleContent.visibility = View.VISIBLE

        // Hide the Refresh Indicator
        binding.thomaeumArticleSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.wordpress_article, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.wordpress_article_share -> {
                if (articleData.link != null) {
                    Log.d("Thomaeum Article", "Trying to Share")
                    val shareIntent = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, articleData.link)
                        type = "text/plain"
                    }, null)
                    startActivity(shareIntent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}