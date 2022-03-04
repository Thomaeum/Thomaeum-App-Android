package net.informatikag.thomapp.utils.handlers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.view.WordpressViewModel
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder
import net.informatikag.thomapp.viewables.viewholders.ThomsLineEndViewholder

/**
 * This class takes care of the RecyclerView that displays the items.
 * @param fragment The fragment in which the RecyclerView is located, through which articles can be loaded
 * @param viewmodel The viewmodel belonging to the fragment, from which the articles are queried
 */
class WordpressRecyclerAdapter(
    val fragment: Fragment,
    val articleClickHandler: ArticleClickHandler,
    val viewmodel:WordpressViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Called when a new viewholder (without data) is created.
     * @param viewType The integer selected by getItemViewtype() which specifies the type of the viewtype.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //The layouts are inflated, depending on the viewType
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                1,
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_article, parent, false),
                fragment,
                true
            )
            1 -> {/* This Case is the same as if the type was unknown*/}
            2 -> return ThomsLineEndViewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_end, parent, false)
            )
        }

        return ThomsLineArticleViewHolder(
            0,
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_article, parent, false),
            fragment,
            true
        )
    }

    /**
     * A content is bound to the viewholder
     * @param holder the ViewHoler to which the content will be bound
     * @param position the position in the Recycler View
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Depending on the type of viewholder there are different procedures
        when(holder){
            // When the viewholder displays an article, it is bound to the corresponding Wordpress article.
            is ThomsLineArticleViewHolder -> {
                // The page on which the content object is located
                val pageIndex = position/(MainActivity.ARTICLES_PER_PAGE)
                // The index at which the content object can be found on the page
                val itemIndex = position%MainActivity.ARTICLES_PER_PAGE

                // The content is bound to the viewHolder
                if (holder.loadingState == 1)
                    holder.bind(viewmodel.articles.value!!
                        .get(pageIndex).articles
                        .get(itemIndex),
                        articleClickHandler
                    )
                else if (holder.loadingState == 0){
                    // If a loading viewholder is to be bound, this means that the end of the page has been
                    // loaded, so further artiels must be loaded, in order not to send too many requests,
                    // this is only done when there are no requests pending.
                    viewmodel.loadArticles(viewmodel.articles.value!!.size, false, fragment.requireContext(), this)
                }
            }
        }
    }

    /**
     * Calculates the number of viewholders in the Recycler View
     * @return viewHolder count
     */
    override fun getItemCount(): Int {
        if (viewmodel.isEmpty()) return 0
        else return (viewmodel.articles.value!!.size-1) * MainActivity.ARTICLES_PER_PAGE + viewmodel.articles.value!![viewmodel.articles.value!!.size-1].articles.size + 1
    }

    /**
     * Calculates the type of viewholder
     * @return 0 = Article Viewholder, 1 = Loading indicator, 2 = End of the RecyclerView
     */
    override fun getItemViewType(position: Int): Int {
        val pageIndex = position/(MainActivity.ARTICLES_PER_PAGE)  // The page on which the data model of the article would be located
        return if (pageIndex >= 0 && position == itemCount-1)
            if (pageIndex == viewmodel.lastPage) 2
            else 1
        else 0
    }
}