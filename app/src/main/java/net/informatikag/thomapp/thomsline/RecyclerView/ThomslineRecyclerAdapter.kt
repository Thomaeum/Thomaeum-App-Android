package net.informatikag.thomapp.thomsline.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.ThomsLineFragmentViewModel
import net.informatikag.thomapp.thomsline.WordpressArticle

class ThomslineRecyclerAdapter(
    val itemClickListener: ItemClickListener,
    val viewModel: ThomsLineFragmentViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var pages: ArrayList<ArrayList<WordpressArticle>> = ArrayList()
    private val perPage:Int = 10

    fun setPages(pPages: ArrayList<ArrayList<WordpressArticle>>){
        pages = pPages
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_article, parent, false),
                itemClickListener
            )
        }
        return ThomsLineLoadingViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_loading, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ThomsLineArticleViewHolder -> {
                var pageIndex = position/(perPage)
                val itemIndex = position%perPage

                holder.bind(pages
                    .get(pageIndex)
                    .get(itemIndex)
                )
            }
            is ThomsLineLoadingViewholder -> {
                viewModel.loadArticles(pages.size+1)
            }
        }
    }

    override fun getItemCount(): Int {
        if (pages.size == 0) return 0
        else return (pages.size-1) * perPage + pages[pages.size-1].size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == getItemCount()-1 && position != 0) return 1
        else return 0
    }
}