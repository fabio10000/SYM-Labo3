package ch.heigvd.iict.sym.labo03.ui.i_beacon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BeaconViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is beacon Fragment"
    }
    val text: LiveData<String> = _text
}