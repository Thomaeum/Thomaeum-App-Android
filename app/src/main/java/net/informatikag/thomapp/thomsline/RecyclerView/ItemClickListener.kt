package net.informatikag.thomapp.thomsline.RecyclerView

import net.informatikag.thomapp.thomsline.WordpressArticle

interface ItemClickListener {
    fun onItemClick(wordpressArticle: WordpressArticle)
}