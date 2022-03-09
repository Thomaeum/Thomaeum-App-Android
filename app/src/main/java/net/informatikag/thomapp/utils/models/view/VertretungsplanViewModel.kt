package net.informatikag.thomapp.utils.models.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.ListView
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.google.android.material.snackbar.Snackbar
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.R
import net.informatikag.thomapp.utils.handlers.SubstitutionListAdapter
import net.informatikag.thomapp.utils.models.data.SubstitutionEntryData
import net.informatikag.thomapp.utils.models.data.SubstitutionProfileData
import net.informatikag.thomapp.utils.models.data.SubstitutionRequstResultData

class VertretungsplanViewModel(application: Application): AndroidViewModel(application) {
    // The Entries
    private val _entrys = MutableLiveData<Array<SubstitutionEntryData>>()
    val entrys: LiveData<Array<SubstitutionEntryData>> = _entrys

    // Profile Data
    private val _profile = MutableLiveData<SubstitutionProfileData>()
    val profile: LiveData<SubstitutionProfileData> = _profile

    fun isEmpty():Boolean = _entrys.value == null

    fun getByDay(day:Int, context: Context, activity: Activity, full: Boolean):Array<SubstitutionEntryData>?{
        if (isEmpty()) loadVertretungsplan(context, activity, full)
        return getByDay(day)
    }

    fun getByDay(day:Int):Array<SubstitutionEntryData>?{
        //TODO Filter by Date
        return _entrys.value
    }

    fun loadVertretungsplan(context: Context, mainActivity: Activity, full:Boolean){
        loadVertretungsplan(context, mainActivity, full) { res, err -> }
    }

    fun loadVertretungsplan(context: Context, mainActivity: Activity, full:Boolean, callback: (SubstitutionRequstResultData?, VolleyError?) -> Unit){
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

        _profile.value!!.getSubstitutionPlan(context, full){results, error ->
            if (error != null || results == null){
                try {
                    Snackbar.make(mainActivity.findViewById(R.id.app_bar_main), MainActivity.getVolleyError(error, mainActivity), Snackbar.LENGTH_LONG).show()
                } catch (e: Exception) {}
            } else {
                _entrys.value = results.entryData
            }
            callback(results, error)
        }
    }

    fun getSize(day:Int):Int = if (isEmpty()) 0 else if (day == 2) _entrys.value!!.size/2 else _entrys.value!!.size

    fun initListView(listView: ListView, context: Context, viewLifecycleOwner:LifecycleOwner, activity: Activity, day: Int, full: Boolean){
        val listViewAdapter = SubstitutionListAdapter(context, this, day)
        entrys.observe(viewLifecycleOwner, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listView.adapter = listViewAdapter
        if(isEmpty()) loadVertretungsplan(context, activity, full)
    }
}