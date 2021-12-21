package net.informatikag.thomapp.viewables.fragments.ThomsLine.article

import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomslineArticleFragmentBinding
import net.informatikag.thomapp.utils.handlers.DrawableImageGetter
import net.informatikag.thomapp.utils.handlers.WordpressHtmlTagHandler
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle
import net.informatikag.thomapp.utils.models.view.ThomsLineViewModel

/**
 * Loads an article from the WordpressAPI and displays title, cover image and content
 */
class ThomsLineArticleFragment : Fragment() {

    private val args: ThomsLineArticleFragmentArgs by navArgs()         // Die Argumente die beim Wechseln zu diesem Fragment übergeben werden
    private var _binding: ThomslineArticleFragmentBinding? = null       // Binding um das Layout zu erreichen
    private lateinit var article: ThomsLineWordpressArticle             // das WordpressArticle Object, welches angezeigt wird
    private val viewmodel: ThomsLineViewModel by activityViewModels()   // Das Viewmodel in dem alle Artikel gespeichert sind

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
        _binding = ThomslineArticleFragmentBinding.inflate(inflater, container, false)

        // Initiate Article Loading and hide Content Containers
        binding.thomslineArticleScrollview.visibility = View.GONE
        val possiblePost = viewmodel.getByID(args.id)
        if (possiblePost!=null) {
            article = possiblePost
            if(article.liteVersion) {
                article.refresh(this.requireContext(), false)
                { article, error -> articleRefreshCallback(article, error) }
                binding.thomslineArticleSwipeRefreshLayout.isRefreshing = true
            } else {
                loadArticleToViews()
            }
        }
        else {
            article = ThomsLineWordpressArticle(args.id, false, this.requireContext())
            { article, error -> articleRefreshCallback(article, error) }
            binding.thomslineArticleSwipeRefreshLayout.isRefreshing = true
        }

        // Initiate Article Loading when a Refresh is triggered
        binding.thomslineArticleSwipeRefreshLayout.setOnRefreshListener {
            TransitionManager.beginDelayedTransition(binding.thomslineArticleScrollview)
            binding.thomslineArticleScrollview.visibility = View.INVISIBLE
            article.refresh(this.requireContext(),false) { article, error ->
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
    fun articleRefreshCallback(article: ThomsLineWordpressArticle?, error: VolleyError?){
        if (error == null) {
            // If there were no Errors just load the Article to the atribute and then to the layout
            this.article = article!!
            loadArticleToViews()
        }
        // If there were Errors, display them in a Snackbar
        else Snackbar.make(
            requireActivity().findViewById(R.id.app_bar_main),
            ThomsLineWordpressArticle.getVolleyError(error, this.requireActivity()),
            Snackbar.LENGTH_LONG
        ).show()
        binding.thomslineArticleSwipeRefreshLayout.isRefreshing = false
    }

    /**
     *  Loads a given WordpressArticle to the Layout
     */
    fun loadArticleToViews() {
        // Make shure the Fragment is still Loaded
        if (this.context == null) return
        binding.thomslineArticleScrollview.visibility = View.VISIBLE

        // Change Title TextView
        binding.thomslineArticleTitle.text = this.article.title
        binding.thomslineArticleTitle.visibility = View.VISIBLE

        // Load Author
        binding.thomslineArticleAuthor.text = this.article.getAuthorString()
        binding.thomslineArticleAuthor.visibility = View.VISIBLE

        // Load Title Image
        val imageView: ImageView = binding.thomslineArticleImage
        if (this.article.imageURL != null)
            Glide.with(imageView.context)
                .load(this.article.imageURL)
                .placeholder(R.drawable.img_thomsline_article_image_default)
                .into(imageView)
        else imageView.visibility = View.GONE

        // Load Date
        binding.thomslineArticleDate.text = "${this.article.date?.hours}:${this.article.date?.minutes} - ${this.article.date?.date}.${this.article.date?.month}.${this.article.date?.year}"
        binding.thomslineArticleDate.visibility = View.VISIBLE

        // Load Content
        val contentView: TextView = binding.thomslineArticleContent
        var content = this.article.content

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

        // Since Android 8 (O) Block-Text is possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) contentView.justificationMode =
            JUSTIFICATION_MODE_INTER_WORD

        // Make the Article visible
        binding.thomslineArticleContent.visibility = View.VISIBLE

        // Hide the Refresh Indicator
        binding.thomslineArticleSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.thomsline_article, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.thomsline_article_share -> {
                if (article.link != null) {
                    Log.d("ThomsLine Article", "Trying to Share")
                    val shareIntent = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, article.link)
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