package net.informatikag.thomapp.utils.models.data

data class ThomsLineWordpressArticlePage(
    val articles:Array<ThomsLineWordpressArticle>
){
    fun equals(page:ThomsLineWordpressArticlePage):Boolean {
        if (articles.size != page.articles.size) return false
        for(i in page.articles){
            if (!articles.contains(i)) return false
        }
        return true
    }

    fun getByID(id:Int):ThomsLineWordpressArticle?{
        for (article in articles) {
            if (article.id == id) return article
        }
        return null
    }
}
