package net.informatikag.thomapp.utils.handlers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.data.VertretungsplanEintrag
import net.informatikag.thomapp.utils.models.view.VertretungsplanViewModel
import org.w3c.dom.Text

class HomeListAdapter(
    context: Context,
    viewModel: VertretungsplanViewModel
): BaseAdapter() {

    private val mContext:Context
    private val viewModel:VertretungsplanViewModel

    init {
        this.mContext = context
        this.viewModel = viewModel
    }

    override fun getCount(): Int {
        return viewModel.getSize()
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.vertretungsplan_list_item, parent, false)
        view.findViewById<TextView>(R.id.test_text).text = viewModel.entrys.value!![position].text
        return view
    }
}