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
import org.w3c.dom.Text

class HomeListAdapter(
    context: Context
): BaseAdapter() {

    private val mContext:Context
    private val arrayList:ArrayList<VertretungsplanEintrag> = ArrayList()

    init {
        this.mContext = context
        loadVertretunsplan()
    }

    fun loadVertretunsplan(){
        for (i in 1..MainActivity.VERTRETUNGSPLAN_PREVIEW_AMOUNT) arrayList.add(VertretungsplanEintrag("Test Eintrag Nr. ${i}"))
        this.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.vertretungsplan_list_item, parent, false)
        view.findViewById<TextView>(R.id.test_text).text = arrayList[position].text
        return view
    }
}