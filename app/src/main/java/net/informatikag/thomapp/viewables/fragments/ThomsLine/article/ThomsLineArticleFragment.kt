package net.informatikag.thomapp.viewables.fragments.ThomsLine.article

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
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

/**
 * Loads an article from the WordpressAPI and displays title, cover image and content
 */
class ThomsLineArticleFragment : Fragment() {

    private val args: ThomsLineArticleFragmentArgs by navArgs()     // Die Argumente die beim Wechseln zu diesem Fragment übergeben werden
    private var _binding: ThomslineArticleFragmentBinding? = null   // Binding um das Layout zu erreichen
    //TODO: Actually use this. At the Moment the Article used is passed through parameters
    private lateinit var article: ThomsLineWordpressArticle         // das WordpressArticle Object, welches angezeigt wird

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        article = ThomsLineWordpressArticle(args.id,this.requireContext())
        { article, error -> articleRefreshCallback(article, error) }

        // Initiate Article Loading when a Refresh is triggered
        binding.thomslineArticleSwipeRefreshLayout.setOnRefreshListener {
            binding.thomslineArticleScrollview.visibility = View.GONE
            article.refresh(this.requireContext()) { article, error ->
                articleRefreshCallback(
                    article,
                    error
                )
            }
        }

        binding.thomslineArticleSwipeRefreshLayout.isRefreshing = true

        return binding.root
    }

    /**
     * Is Called when the Article was refreshed
     */
    fun articleRefreshCallback(article: ThomsLineWordpressArticle, error: VolleyError?){
        // If there were no Errors just load the Article to the Layout
        if (error == null) loadArticleToViews(article)
        // If there were Errors, display them in a Snackbar
        else Snackbar.make(
            requireActivity().findViewById(R.id.app_bar_main),
            ThomsLineWordpressArticle.getVolleyError(error, this.requireActivity()),
            Snackbar.LENGTH_LONG
        ).show()
        // Hide the Refresh Indicator
        binding.thomslineArticleSwipeRefreshLayout.isRefreshing = false
    }

    /**
     *  Loads a given WordpressArticle to the Layout
     */
    fun loadArticleToViews(wordpressArticle: ThomsLineWordpressArticle) {
        // Make shure the Fragment is still Loaded
        if (this.context == null) return
        
        // Change Title TextView
        binding.thomslineArtilcleTitle.setText(wordpressArticle.title)

        // Load Author
        binding.thomslineArticleAuthor.setText(wordpressArticle.getAuthorString())

        // Load Date
        binding.thomslineArticleDate.setText(
            "${wordpressArticle.date?.hours}:${wordpressArticle.date?.minutes} - ${wordpressArticle.date?.date}.${wordpressArticle.date?.month}.${wordpressArticle.date?.year}"
        )

        // Load Title Image
        val imageView: ImageView = binding.thomslineArticleImage
        if (wordpressArticle.imageURL != null)
            Glide.with(imageView.context)
                .load(wordpressArticle.imageURL)
                .placeholder(R.drawable.img_thomsline_article_image_default)
                .into(imageView)
        else imageView.visibility = View.GONE

        // Load Content
        val contentView: TextView = binding.thomslineArticleContent
        var content = wordpressArticle.content

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) contentView.justificationMode = JUSTIFICATION_MODE_INTER_WORD

        // Make the Article visible
        binding.thomslineArticleScrollview.visibility = View.VISIBLE
    }
}