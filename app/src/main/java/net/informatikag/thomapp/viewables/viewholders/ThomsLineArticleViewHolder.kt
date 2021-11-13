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
 * Viewholder der einen Artikel anzeigt
 * @param itemView Layout des Viewholders
 * @param fragment Fragment in dem der Viewholer angezeigt wird. Das wird für den Context und das
 *  Navigieren zur Detail Ansicht des Artikels benötigt
 */
class ThomsLineArticleViewHolder constructor(
    itemView: View,
    val fragment: ThomsLineFragment
): RecyclerView.ViewHolder(itemView){

    /**
     * Diese Methode wird aufgerufen wenn ein Artikel an den Viewholder gebunden wird, hier werden
     * also die Inhalte in das Layout geladen
     */
    fun bind(post: ThomsLineWordpressArticle){

        // Artikel und Unterüberschrift setzen
        itemView.findViewById<TextView>(R.id.thomsline_post_title).setText(post.title)
        itemView.findViewById<TextView>(R.id.thomsline_post_excerpt).setText(post.excerpt)

        // Wenn es eine ImageURL gibt wird diese geladen, ansonsten wird das standard Bild eingefügt
        if (post.imageURL != null)
        Glide.with(itemView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .load(post.imageURL)
            .placeholder(R.drawable.img_thomsline_article_image_default)
            .into(itemView.findViewById<ImageView>(R.id.thomsline_post_image))
        else itemView.findViewById<ImageView>(R.id.thomsline_post_image).setImageDrawable(
            AppCompatResources.getDrawable(
                fragment.requireContext(),
                R.drawable.img_thomsline_article_image_default
            ))

        // Ein OnClickListener wird hinzugefügt, um bei einem Click in die Detailansicht wechseln zu können
        itemView.setOnClickListener(View.OnClickListener {
            fragment.onItemClick(post)
        })
    }
}