package net.informatikag.thomapp.thomsline.models

data class WordpressArticle(
    var id: Int,
    var title: String,
    var content: String,
    var excerpt: String,
    var author: String,
    var imageURL: String,
)