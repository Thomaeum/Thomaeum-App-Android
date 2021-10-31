package net.informatikag.thomapp.thomsline

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.models.WordpressArticle

class ThomslineRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<WordpressArticle> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThomsLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ThomsLineViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(postList: ArrayList<WordpressArticle>){
        items = postList
        this.notifyDataSetChanged()
    }

    class ThomsLineViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.thomsline_post_title)
        val author = itemView.findViewById<TextView>(R.id.thomsline_post_author)
        val image = itemView.findViewById<ImageView>(R.id.thomsline_post_image)

        fun bind(post: WordpressArticle){
            Log.d("ThomsLineAdapter", post.title)
            title.setText(post.title)
            author.setText(post.author)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(post.imageURL)
                .into(image)
        }
    }
}