package net.informatikag.thomapp.utils.models.data

data class WordpressPage(
    val articles:Array<WordpressArticle>
){
    fun equals(page:WordpressPage):Boolean {
        if (articles.size != page.articles.size) return false
        for(i in page.articles){
            if (!articles.contains(i)) return false
        }
        return true
    }

    fun getByID(id:Int):WordpressArticle?{
        for (article in articles) {
            if (article.id == id) return article
        }
        return null
    }
}
