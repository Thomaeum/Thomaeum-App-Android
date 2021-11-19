package net.informatikag.thomapp.viewables.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.viewables.fragments.ThomsLine.main.ThomsLineFragment
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

/**
 * Viewholder displaying an article
 * @param itemView Viewholder layout
 * @param fragment Fragment in which the viewholer is displayed. This is needed for the context and
 * navigating to the detail view of the article
 */
class ThomsLineArticleViewHolder constructor(
    itemView: View,
    val fragment: ThomsLineFragment
): RecyclerView.ViewHolder(itemView){

    /**
     * This method is called when an article is bound to the viewholder, so here the content is
     * loaded into the layout
     */
    fun bind(post: ThomsLineWordpressArticle){

        // Set article and subheading
        itemView.findViewById<TextView>(R.id.thomsline_post_title).setText(post.title)
        itemView.findViewById<TextView>(R.id.thomsline_post_excerpt).setText(post.excerpt)

        // If there is an ImageURL it will be loaded, otherwise the default image will be inserted.
        if (post.imageURL != null)
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error))
            .load(post.imageURL)
            .placeholder(R.drawable.img_thomsline_article_image_default)
            .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))
        else itemView.findViewById<ImageView>(R.id.thomsline_post_image).setImageDrawable(
            AppCompatResources.getDrawable(
                fragment.requireContext(),
                R.drawable.img_thomsline_article_image_default
            ))

        // An OnClickListener is added to be able to switch to the detail view on clicking
        itemView.setOnClickListener(View.OnClickListener {
            fragment.onItemClick(post)
        })
    }
}