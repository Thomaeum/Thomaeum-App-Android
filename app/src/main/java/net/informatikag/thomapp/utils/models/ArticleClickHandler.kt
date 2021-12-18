package net.informatikag.thomapp.utils.models

import net.informatikag.thomapp.utils.models.data.ThomsLineWordpressArticle

interface ArticleClickHandler {
    fun onItemClick(thomsLineWordpressArticle: ThomsLineWordpressArticle)
}