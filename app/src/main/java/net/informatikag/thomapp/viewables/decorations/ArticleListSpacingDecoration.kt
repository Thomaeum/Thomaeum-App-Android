package net.informatikag.thomapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Diese Decoration wird auf jedes Element im Recyclerview angewendet, vorallem um abstand zwischen
 * den Artikeln und zu den Bildschrimrändern zu haben
 */
class ArticleListSpacingDecoration: RecyclerView.ItemDecoration(){

    private val PADDING: Int = 30

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            top = PADDING
            bottom = PADDING
            left = PADDING
            right = PADDING
        }
    }
}