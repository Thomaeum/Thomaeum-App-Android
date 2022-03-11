package net.informatikag.thomapp.utils.handlers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.models.view.SubstitutionViewModel
import net.informatikag.thomapp.viewables.viewholders.SubstitutionEntryViewholder
import org.w3c.dom.Text

class SubstitutionListAdapter(
    private val context: Context,
    private val viewModel: SubstitutionViewModel,
    private val day: Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubstitutionEntryViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.substitution_list_item, parent, false),
            context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SubstitutionEntryViewholder -> holder.bind(viewModel.getByDay(this.day)!![position])
        }
    }

    override fun getItemCount(): Int = viewModel.getByDay(day)?.size ?:0
}