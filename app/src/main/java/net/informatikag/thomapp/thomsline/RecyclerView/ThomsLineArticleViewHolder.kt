package net.informatikag.thomapp.thomsline.RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.WordpressArticle

class ThomsLineArticleViewHolder constructor(
    itemView: View,
    val itemClickListener: ItemClickListener
): RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.thomsline_post_title)
    val author = itemView.findViewById<TextView>(R.id.thomsline_post_author)
    val image = itemView.findViewById<ImageView>(R.id.thomsline_post_image)

    fun bind(post: WordpressArticle){
        title.setText(post.title)
        author.setText(post.author)

        if (post.imageURL != null)
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .load(post.imageURL)
            .placeholder(R.drawable.default_article_image)
            .into(image)

        itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(post)
        })
    }
}