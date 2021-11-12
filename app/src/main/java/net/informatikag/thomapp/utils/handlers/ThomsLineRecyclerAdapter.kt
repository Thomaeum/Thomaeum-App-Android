package net.informatikag.thomapp.utils.handlers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.viewables.fragments.ThomsLine.main.ThomsLineFragment
import net.informatikag.thomapp.viewables.viewholders.ThomsLineArticleViewHolder
import net.informatikag.thomapp.viewables.viewholders.ThomsLineLoadingViewholder
import net.informatikag.thomapp.utils.models.view.ThomsLineFragmentViewModel
import net.informatikag.thomapp.viewables.viewholders.ThomsLineEndViewholder

/**
 * Diese Klasse kümmert sich um den RecyclerView der die Artikel anzeigt.
 * @param fragment Das Fragment in dem der RecyclerView sich befindet, wudurch Artikel geladen werden können
 * @param viewmodel Das zum Fragment gehörige Viewmodel, woraus die Artikel abgefragt werden
 */
class ThomsLineRecyclerAdapter(
    val fragment: ThomsLineFragment,
    val viewmodel:ThomsLineFragmentViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Anzahl der Artikel pro Seite
    //TODO dies sollte eine Globale Variable werden
    private val perPage:Int = 10

    /**
     * Wird aufgerufen wenn ein neuer Viewholder (ohne Daten) erstellt wird.
     * @param viewType Der durch getItemViewtype() ausgewöhlte Int der die Sorte des Viewtyps angibt
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        //Die Layouts werden Inflatet, je nach viewType
        when(viewType) {
            0 -> return ThomsLineArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_article, parent, false),
                fragment
            )
            1 -> return ThomsLineLoadingViewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_loading, parent, false)
            )
            2 -> return ThomsLineEndViewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_end, parent, false)
            )
        }
        //Wenn der Viewtype nicht bekannt ist wird einfach ein Ladesymbol geladen
        return ThomsLineLoadingViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.thomsline_main_recyclerview_loading, parent, false)
        )
    }

    /**
     * Ein Inhalt wird an den Viewholder gebunden
     * @param holder der ViewHoler an den der Inhalt gebunden wird
     * @param position die Position im Recycler View
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Je nach Typ des Viewholders gibt es verschiedene Prozeduren
        when(holder){
            //Wenn der Viewholder einen Artikel Darstellt wird er an den entsprechenden WordpressArtikel gebunden
            is ThomsLineArticleViewHolder -> {
                //Die Seite auf der das inhaltsObjekt liegt
                val pageIndex = position/(perPage)
                //Der Index an dem das inhaltsObjekt auf der Seite zu finden ist
                val itemIndex = position%perPage

                //Der Inhalt wird an den viewHolder gebunden
                holder.bind(viewmodel.articles.value!!
                    .get(pageIndex)
                    .get(itemIndex)
                )
            }
            // Wenn ein Lade Viewholder gebunden werden soll, heißt das das das Ende der Seite
            // geladen wurde, es müssen also weitere Artiel nachgeladen werden, um nicht zu viele
            // anfragen zu senden, wird das nur gemacht wenn gerade keine Anfragen ausstehen
            is ThomsLineLoadingViewholder -> {
                if (!fragment.isLoading()) fragment.loadArticles(viewmodel.articles.value!!.size)
            }
        }
    }

    /**
     * Berechnet die Anzahl der Viewholder im Recycler View
     * @return viewHolder Anzahl
     */
    override fun getItemCount(): Int {
        if (viewmodel.articles.value == null || viewmodel.articles.value?.size == 0) return 0
        else return (viewmodel.articles.value!!.size-1) * perPage + viewmodel.articles.value!![viewmodel.articles.value!!.size-1].size + 1
    }

    /**
     * Berechnet den Typ des Viewholders
     * @return 0 = Article Viewholder, 1 = Lade Anzeige, 2 = Ende der Artikel
     */
    override fun getItemViewType(position: Int): Int {
        val pageIndex = position/(perPage)  //Die Seite auf der das Daten Model eines Artikel liegen würde
        return if ((pageIndex != 0 && position == itemCount-1)) if (pageIndex == viewmodel.lastPage) 2 else 1 else 0
    }
}