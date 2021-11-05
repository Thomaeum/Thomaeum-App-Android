package net.informatikag.thomapp.thomsline.utils

data class WordpressArticle(
    var id: Int,
    var title: String,
    var content: String,
    var excerpt: String,
    var author: String,
    var imageURL: String?,
)
