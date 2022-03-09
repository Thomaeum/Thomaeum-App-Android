package net.informatikag.thomapp.utils.handlers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.view.SubstitutionViewModel

class SubstitutionListAdapter(
    context: Context,
    viewModel: SubstitutionViewModel,
    day: Int
): BaseAdapter() {

    private val mContext:Context
    private val viewModel:SubstitutionViewModel
    private val day: Int

    init {
        this.mContext = context
        this.viewModel = viewModel
        this.day = day
    }

    override fun getCount(): Int = viewModel.getByDay(day)?.size ?: 0

    override fun getItem(position: Int): Any = viewModel.getByDay(this.day)!![position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.substitution_list_item, parent, false)

        val substitutionEntryData = viewModel.getByDay(this.day)!![position]
        if (substitutionEntryData.duration == 1)
            view.findViewById<TextView>(R.id.substitution_lesson_textview).text = mContext.getString(R.string.substitution_item_lesson_time_single, substitutionEntryData.start)
        else
            view.findViewById<TextView>(R.id.substitution_lesson_textview).text = mContext.getString(R.string.substitution_item_lesson_time_double, substitutionEntryData.start, substitutionEntryData.start + substitutionEntryData.duration - 1)

        view.findViewById<TextView>(R.id.substitution_room_textview).text = substitutionEntryData.room
        return view
    }
}