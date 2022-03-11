package net.informatikag.thomapp.viewables.viewholders

import android.content.Context
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.SubstitutionEntryData
import net.informatikag.thomapp.utils.models.data.WordpressArticleData

/**
 * Viewholder displaying an article
 * @param itemView Viewholder layout
 * @param fragment Fragment in which the viewholer is displayed. This is needed for the context and
 * navigating to the detail view of the article
 */
class SubstitutionEntryViewholder constructor(
    itemView: View,
    val context: Context
): RecyclerView.ViewHolder(itemView){

    private var extended = false
        set(value) {
            field
        }

    /**
     * This method is called when an article is bound to the viewholder, so here the content is
     * loaded into the layout
     */
    fun bind(entryData: SubstitutionEntryData){

        // Time
        if (entryData.duration == 1)
            itemView.findViewById<TextView>(R.id.substitution_time_textview).text = context.getString(R.string.substitution_item_lesson_time_single, entryData.start)
        else
            itemView.findViewById<TextView>(R.id.substitution_time_textview).text = context.getString(R.string.substitution_item_lesson_time_double, entryData.start, entryData.start + entryData.duration - 1)

        // Teacher
        itemView.findViewById<TextView>(R.id.substitution_teacher_textview).text = entryData.teacher

        // Course
        itemView.findViewById<TextView>(R.id.substitution_course_textview).text = context.getString(R.string.substitution_item_lesson_course,
            entryData.regularCourse.subject,
            if (entryData.regularCourse.courseType) "LK" else "GK",
            entryData.room
        )

        // Type
        itemView.findViewById<TextView>(R.id.substitution_type_textview).text = context.getString(R.string.substitution_item_lesson_type, entryData.type)

        // Annotations
        itemView.findViewById<TextView>(R.id.substitution_annotation_textview).text = context.getString(R.string.substitution_item_lesson_annotations, entryData.annotations)

        val detailsLayout = itemView.findViewById<LinearLayout>(R.id.substitution_details)
        itemView.setOnClickListener {
            detailsLayout.visibility = if (detailsLayout.visibility != View.GONE) View.GONE else View.VISIBLE
        }
    }
}