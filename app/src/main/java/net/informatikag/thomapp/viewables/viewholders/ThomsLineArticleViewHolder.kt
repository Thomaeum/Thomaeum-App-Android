package net.informatikag.thomapp.viewables.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.viewables.fragments.ThomsLine.main.ThomsLineFragment
import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

class ThomsLineArticleViewHolder constructor(
    itemView: View,
    val fragment: ThomsLineFragment
): RecyclerView.ViewHolder(itemView){
    fun bind(post: ThomsLineWordpressArticle){
        itemView.findViewById<TextView>(R.id.thomsline_post_title).setText(post.title)
        itemView.findViewById<TextView>(R.id.thomsline_post_author).setText(post.getAuthorString())

        if (post.imageURL != null)
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .load(post.imageURL)
            .placeholder(R.drawable.img_thomsline_article_image_default)
            .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))

        itemView.setOnClickListener(View.OnClickListener {
            fragment.onItemClick(post)
        })
    }
}