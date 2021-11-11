package net.informatikag.thomapp.utils.handlers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.viewables.fragments.ThomsLine.main.ThomsLineFragment
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder
import net.informatikag.thomapp.viewables.viewholders.ThomsLineLoadingViewholder
import net.informatikag.thomapp.utils.models.view.ThomsLineFragmentViewModel
import net.informatikag.thomapp.viewables.viewholders.ThomsLineEndViewholder

class ThomsLineRecyclerAdapter(
    val fragment: ThomsLineFragment,
    val viewmodel:ThomsLineFragmentViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val perPage:Int = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_article, parent, false),
                fragment
            )
            2 -> return ThomsLineEndViewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_end, parent, false)
            )
        }
        return ThomsLineLoadingViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_loading, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ThomsLineArticleViewHolder -> {
                val pageIndex = position/(perPage)
                val itemIndex = position%perPage

                holder.bind(viewmodel.articles.value!!
                    .get(pageIndex)
                    .get(itemIndex)
                )
            }
            is ThomsLineLoadingViewholder -> {
                if (!fragment.isLoading()) fragment.loadArticles(viewmodel.articles.value!!.size)
            }
        }
    }

    override fun getItemCount(): Int {
        if (viewmodel.articles.value == null || viewmodel.articles.value?.size == 0) return 0
        else return (viewmodel.articles.value!!.size-1) * perPage + viewmodel.articles.value!![viewmodel.articles.value!!.size-1].size + 1
    }

    /**
     * 1 if Viewholder is a Loadingindicator
     * 0 if Viewholder is an Article
     */
    override fun getItemViewType(position: Int): Int {
        val pageIndex = position/(perPage)
        return if ((pageIndex != 0 && position == itemCount-1)) if (pageIndex == viewmodel.lastPage) 2 else 1 else 0
    }
}