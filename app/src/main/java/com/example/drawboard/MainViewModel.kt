package com.example.drawboard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainViewModel:ViewModel() {
    var colorList = arrayListOf<FloatingActionButton>()
    var isColorOpen = MutableLiveData(false)
    var showPanSize = MutableLiveData(false)
    var imageUri: Uri? = null
}