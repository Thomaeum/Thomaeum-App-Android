package net.informatikag.thomapp.utils.models

import net.informatikag.thomapp.utils.models.data.WordpressArticleData

interface ArticleClickHandler {
    fun onItemClick(wordpressArticleData: WordpressArticleData)
}