package net.informatikag.thomapp.utils.handlers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.view.VertretungsplanViewModel

class SubstitutionListAdapter(
    context: Context,
    viewModel: VertretungsplanViewModel,
    day: Int
): BaseAdapter() {

    private val mContext:Context
    private val viewModel:VertretungsplanViewModel
    private val day: Int

    init {
        this.mContext = context
        this.viewModel = viewModel
        this.day = day
    }

    override fun getCount(): Int {
        return viewModel.getSize(day)
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.substitution_list_item, parent, false)

        val substitutionEntryData = viewModel.getByDay(this.day)!![position]
        if (substitutionEntryData.range == 1)
            view.findViewById<TextView>(R.id.substitution_lesson_textview).text = "${substitutionEntryData.start}. Stunde"
        else
            view.findViewById<TextView>(R.id.substitution_lesson_textview).text = "${substitutionEntryData.start}-${substitutionEntryData.start + substitutionEntryData.range - 1}. Stunde"

        view.findViewById<TextView>(R.id.substitution_room_textview).text = substitutionEntryData.room
        return view
    }
}