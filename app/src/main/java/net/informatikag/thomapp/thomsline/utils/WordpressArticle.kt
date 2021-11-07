package net.informatikag.thomapp.thomsline.utils

data class WordpressArticle(
    var id: Int,
    var title: String,
    var content: String,
    var excerpt: String,
    var authors: Array<String>,
    var imageURL: String?,
) {
    fun getAuthorString():String{
        var output = ""
        for (i in 0 until authors.size) {
            output += authors[i]
            output += if (i+2 == authors.size) " und " else if (i+1 == authors.size) "" else ", "
            output += when(i) {
                authors.size-2 -> " und "
                authors.size-1 -> ""
                else -> ", "
            }
        }
        return output
    }
}
