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

class SubstitutionViewModel(application: Application): AndroidViewModel(application) {
    // The Entries
    private val _entrys = MutableLiveData<Array<SubstitutionEntryData>>()
    val entrys: LiveData<Array<SubstitutionEntryData>> = _entrys

    // Profile Data
    private val _profile = MutableLiveData<SubstitutionProfileData>()
    val profile: LiveData<SubstitutionProfileData> = _profile

    fun isEmpty():Boolean = _entrys.value == null

    fun getByDay(day:Int, context: Context, activity: Activity, full: Boolean):Array<SubstitutionEntryData>?{
        if (isEmpty()) loadSubstitution(context, activity, full)
        return getByDay(day)
    }

    fun getByDay(day:Int):Array<SubstitutionEntryData>?{
        if (_entrys.value != null) {
            val entryData = ArrayList<SubstitutionEntryData>(0)
            _entrys.value!!.forEach {
                if (getRelativDate(it.date) == day)
                    entryData.add(it)
            }
            return entryData.toTypedArray()
        }
        return null
    }

    fun loadSubstitution(context: Context, mainActivity: Activity, full:Boolean){
        loadSubstitution(context, mainActivity, full) { res, err -> }
    }

    fun loadSubstitution(context: Context, mainActivity: Activity, full:Boolean, callback: (SubstitutionRequstResultData?, VolleyError?) -> Unit){
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

        _profile.value!!.getSubstitution(context, full){ results, error ->
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

    fun initListView(listView: ListView, context: Context, viewLifecycleOwner:LifecycleOwner, activity: Activity, day: Int, full: Boolean){
        if(isEmpty()) loadSubstitution(context, activity, full)

        val listViewAdapter = SubstitutionListAdapter(context, this, day)
        entrys.observe(viewLifecycleOwner, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listView.adapter = listViewAdapter
        listView.dividerHeight = 10
    }

    fun getRelativDate(timeStamp:Long): Int{
        return getDaysFromTimestamp(timeStamp) - getDaysFromTimestamp(System.currentTimeMillis())
    }

    private fun getDaysFromTimestamp(stamp: Long): Int {
        var r: Int = (stamp / 86400000).toInt()
        r -= r % 1
        return r
    }
}