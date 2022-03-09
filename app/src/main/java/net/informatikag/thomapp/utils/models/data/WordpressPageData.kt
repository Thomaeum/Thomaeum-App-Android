package net.informatikag.thomapp.utils.models.data

data class WordpressPageData(
    val articleData:Array<WordpressArticleData>
){
    fun equals(pageData:WordpressPageData):Boolean {
        if (articleData.size != pageData.articleData.size) return false
        for(i in pageData.articleData){
            if (!articleData.contains(i)) return false
        }
        return true
    }

    fun getByID(id:Int):WordpressArticleData?{
        for (article in articleData) {
            if (article.id == id) return article
        }
        return null
    }
}
