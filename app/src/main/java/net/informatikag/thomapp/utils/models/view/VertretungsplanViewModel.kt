package net.informatikag.thomapp.utils.models.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.utils.models.data.SubstitutionEntryData
import net.informatikag.thomapp.utils.models.data.SubstitutionProfileData

class VertretungsplanViewModel(application: Application): AndroidViewModel(application) {
    // The Entries
    private val _entrys = MutableLiveData<ArrayList<SubstitutionEntryData>>()
    val entrys: LiveData<ArrayList<SubstitutionEntryData>> = _entrys

    // Profile Data
    private val _profile = MutableLiveData<SubstitutionProfileData>()
    val profile: LiveData<SubstitutionProfileData> = _profile

    fun isEmpty():Boolean = _entrys.value == null

    fun loadVertretunsplan(context: Context){
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
            if (error != null){
                // TODO add Error handling
            } else {
                for (entry in results!!.entryData) {
                    if (!isEmpty())
                        _entrys.value!!.add(entry)
                    else
                        _entrys.value = arrayListOf(entry)
                }
            }
        }
    }

    fun getSize():Int = if (isEmpty()) 0 else _entrys.value!!.size

}