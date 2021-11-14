package net.informatikag.thomapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * This decoration is applied to each element in the recyclerview, mainly to have distance between the items and to the edges of the screen.
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