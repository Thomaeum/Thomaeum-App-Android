package net.informatikag.thomapp.utils.models.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.ListView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.handlers.SubstitutionListAdapter
import net.informatikag.thomapp.utils.models.data.SubstitutionEntryData
import net.informatikag.thomapp.utils.models.data.SubstitutionProfileData

class VertretungsplanViewModel(application: Application): AndroidViewModel(application) {
    // The Entries
    private val _entrys = MutableLiveData<Array<SubstitutionEntryData>>()
    val entrys: LiveData<Array<SubstitutionEntryData>> = _entrys

    // Profile Data
    private val _profile = MutableLiveData<SubstitutionProfileData>()
    val profile: LiveData<SubstitutionProfileData> = _profile

    fun isEmpty():Boolean = _entrys.value == null

    fun getByDay(day:Int, context: Context, activity: Activity):Array<SubstitutionEntryData>?{
        if (isEmpty()) loadVertretunsplan(context, activity)
        //TODO Filter by Date
        return _entrys.value
    }

    fun loadVertretunsplan(context: Context, mainActivity: Activity){
        // TODO replace this with letting the user Choose a Profile
        _profile.value = SubstitutionProfileData(
            -1,
            Array<SubstitutionProfileData.SubstitutionProfileEntryData>(0) {i ->
                SubstitutionProfileData.SubstitutionProfileEntryData(
                    null,
                    null,
                    null,
                    null,
                    null
                )
            }
        )

        _profile.value!!.getSubstitutionPlan(context){results, error ->
            if (error != null || results == null){
                try {
                    Snackbar.make(mainActivity.findViewById(R.id.app_bar_main), MainActivity.getVolleyError(error, mainActivity), Snackbar.LENGTH_LONG).show()
                } catch (e: Exception) {}
            } else {
                _entrys.value = results.entryData
            }
        }
    }

    fun getSize():Int = if (isEmpty()) 0 else _entrys.value!!.size

    fun initListView(listView: ListView, context: Context, viewLifecycleOwner:LifecycleOwner, activity: Activity){
        val listViewAdapter = SubstitutionListAdapter(context, this)
        entrys.observe(viewLifecycleOwner, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listView.adapter = listViewAdapter
        if(isEmpty()) loadVertretunsplan(context, activity)
    }
}