package net.informatikag.thomapp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VertretungsplanUnterstufeViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://www.thomaeum.de/wp-content/uploads/2020/10/thom_si.pdf"
    }
    val url: LiveData<String> = _url
}