package net.informatikag.thomapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.MainActivity

/**
 * This decoration is applied to each element in the recyclerview, mainly to have distance between the items and to the edges of the screen.
 */
class ArticleListSpacingDecoration: RecyclerView.ItemDecoration(){


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            top = MainActivity.THOMSLINE_LIST_ARTICLE_PADDING
            bottom = MainActivity.THOMSLINE_LIST_ARTICLE_PADDING
            left = MainActivity.THOMSLINE_LIST_ARTICLE_PADDING
            right = MainActivity.THOMSLINE_LIST_ARTICLE_PADDING
        }
    }
}