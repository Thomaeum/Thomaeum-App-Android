package net.informatikag.thomapp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThomsLineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Thomsline Fragment"
    }
    val text: LiveData<String> = _text
}