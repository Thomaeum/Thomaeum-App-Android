package net.informatikag.thomapp.utils.models

import net.informatikag.thomapp.utils.models.data.WordpressArticle

interface ArticleClickHandler {
    fun onItemClick(wordpressArticle: WordpressArticle)
}