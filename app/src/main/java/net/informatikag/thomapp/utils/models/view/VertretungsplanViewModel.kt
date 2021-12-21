package net.informatikag.thomapp.utils.models.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.informatikag.thomapp.MainActivity
import net.informatikag.thomapp.utils.models.data.VertretungsplanEintrag

class VertretungsplanViewModel(application: Application): AndroidViewModel(application) {
    // The articles
    private val _entrys = MutableLiveData<ArrayList<VertretungsplanEintrag>>()
    val entrys: LiveData<ArrayList<VertretungsplanEintrag>> = _entrys

    fun isEmpty():Boolean = _entrys.value == null

    fun loadVertretunsplan(){
        for (i in 1..MainActivity.VERTRETUNGSPLAN_PREVIEW_AMOUNT) {
            if (!isEmpty())
                _entrys.value!!.add(VertretungsplanEintrag("Test Eintrag Nr. ${i}"))
            else
                _entrys.value = arrayListOf(VertretungsplanEintrag("Test Eintrag Nr. ${i}"))
        }
    }

    fun getSize():Int = if (isEmpty()) 0 else _entrys.value!!.size

}