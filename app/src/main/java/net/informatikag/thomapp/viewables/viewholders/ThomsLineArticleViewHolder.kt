package net.informatikag.thomapp.viewables.viewholders

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticle

/**
 * Viewholder displaying an article
 * @param itemView Viewholder layout
 * @param fragment Fragment in which the viewholer is displayed. This is needed for the context and
 * navigating to the detail view of the article
 */
class ThomsLineArticleViewHolder constructor(
    state:Int,
    itemView: View,
    val fragment: Fragment,
    val showExcerpt: Boolean,

): RecyclerView.ViewHolder(itemView){
    var loadingState:Int = 0
        set(value) {
            TransitionManager.beginDelayedTransition(itemView as ViewGroup?)
            field = value
            when (value) {
                -1 -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.hideShimmer()
                    imageView.visibility = View.VISIBLE
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            fragment.requireContext(),
                            R.drawable.ic_wordpress_article_error
                        )
                    )

                    titleView.visibility = View.VISIBLE
                    titleView.background = null
                    titleView.text = fragment.activity?.getString(R.string.network_error_generic)

                    excerptView.visibility = View.GONE
                    excerptView.background = null

                    itemView.findViewById<ProgressBar>(R.id.thomsline_post_loading_indicator).visibility =
                        View.GONE
                }
                0 -> {
                    shimmerLayout.showShimmer(true)

                    imageView.visibility = View.VISIBLE

                    titleView.visibility = View.VISIBLE
                    titleView.background = fragment.requireContext().resources.getDrawable(R.color.secondaryDarkColor)

                    excerptView.visibility = View.VISIBLE
                    excerptView.background = fragment.requireContext().resources.getDrawable(R.color.secondaryDarkColor)

                    itemView.findViewById<ProgressBar>(R.id.thomsline_post_loading_indicator).visibility =
                        View.INVISIBLE
                }
                1 -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.hideShimmer()
                    imageView.visibility = View.VISIBLE

                    titleView.visibility = View.VISIBLE
                    titleView.background = null

                    excerptView.visibility = View.VISIBLE
                    excerptView.background = null

                    itemView.findViewById<ProgressBar>(R.id.thomsline_post_loading_indicator).visibility =
                    View.GONE
                }
            }
            if (!showExcerpt) excerptView.visibility = View.GONE
        }

    private val shimmerLayout: ShimmerFrameLayout
        get() = itemView.findViewById(R.id.thomsline_post_shimmerLayout)

    private val titleView:TextView
        get() = itemView.findViewById(R.id.thomsline_post_title)

    private val excerptView:TextView
        get() = itemView.findViewById(R.id.thomsline_post_excerpt)

    private val imageView:ImageView
        get() = itemView.findViewById(R.id.thomsline_post_image)

    init {
        this.loadingState = state
    }

    /**
     * This method is called when an article is bound to the viewholder, so here the content is
     * loaded into the layout
     */
    fun bind(post: WordpressArticle, clickHandler: ArticleClickHandler){
        // Set article and subheading
        itemView.findViewById<TextView>(R.id.thomsline_post_title).setText(post.title)
        itemView.findViewById<TextView>(R.id.thomsline_post_excerpt).setText(post.excerpt)

        // If there is an ImageURL it will be loaded, otherwise the default image will be inserted.
        if (post.imageURL != null)
            Picasso.get()
                .load(post.imageURL)
                .placeholder(R.drawable.ic_wordpress_article_default)
                .error(R.drawable.ic_wordpress_article_error)
                .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))
//        Glide.with(itemView.context)
//            .applyDefaultRequestOptions(
//                RequestOptions()
//                    .placeholder(R.drawable.ic_wordpress_article_default)
//                    .error(R.drawable.ic_wordpress_article_error))
//            .load(post.imageURL)
//            .placeholder(R.drawable.ic_wordpress_article_default)
//            .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))
        else itemView.findViewById<ImageView>(R.id.thomsline_post_image).setImageDrawable(
            AppCompatResources.getDrawable(
                fragment.requireContext(),
                R.drawable.ic_wordpress_article_default
            ))

        // An OnClickListener is added to be able to switch to the detail view on clicking
        itemView.setOnClickListener {clickHandler.onItemClick(post)}
        loadingState = 1
    }
}