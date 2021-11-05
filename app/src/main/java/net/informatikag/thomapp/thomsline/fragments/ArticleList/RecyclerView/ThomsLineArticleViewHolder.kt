package net.informatikag.thomapp.thomsline.fragments.ArticleList.RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.utils.ItemClickListener
import net.informatikag.thomapp.thomsline.utils.WordpressArticle

class ThomsLineArticleViewHolder constructor(
    itemView: View,
    val itemClickListener: ItemClickListener
): RecyclerView.ViewHolder(itemView){
    fun bind(post: WordpressArticle){
        itemView.findViewById<TextView>(R.id.thomsline_post_title).setText(post.title)
        itemView.findViewById<TextView>(R.id.thomsline_post_author).setText(post.author)

        if (post.imageURL != null)
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .load(post.imageURL)
            .placeholder(R.drawable.default_article_image)
            .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))

        itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(post)
        })
    }
}