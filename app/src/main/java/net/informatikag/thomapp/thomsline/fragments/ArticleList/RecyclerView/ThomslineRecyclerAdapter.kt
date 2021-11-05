package net.informatikag.thomapp.thomsline.fragments.ArticleList.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.thomsline.fragments.ArticleList.ThomsLineFragment
import net.informatikag.thomapp.thomsline.utils.WordpressArticle

class ThomslineRecyclerAdapter(
    val fragment: ThomsLineFragment,
//    val itemClickListener: ItemClickListener,
//    val viewModel: ThomsLineFragmentViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var pages: ArrayList<ArrayList<WordpressArticle>> = ArrayList()
    private val perPage:Int = 10
    private var lastPage:Int = -1

    fun setPages(pPages: ArrayList<ArrayList<WordpressArticle>>, pLastPage: Int){
        this.pages = pPages
        this.lastPage = pLastPage
        Log.d("test", pLastPage.toString())
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_list_article, parent, false),
                fragment
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
                fragment.loadArticles(pages.size+1)
            }
        }
    }

    override fun getItemCount(): Int {
        if (pages.size == 0) return 0
        else return (pages.size-1) * perPage + pages[pages.size-1].size + (if (pages.size >= lastPage && lastPage != -1) 0 else 1)
    }

    /*
    1 if Viewholder is a Loadingindicator
    0 if Viewholder is an Article
     */
    override fun getItemViewType(position: Int): Int {
        val pageIndex = position/(perPage)
        val itemIndex = position%perPage
        return if ((pageIndex != 0 && position == itemCount-1) && (pageIndex != lastPage)) 1
        else 0
    }
}