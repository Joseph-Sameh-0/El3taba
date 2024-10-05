package com.example.el3taba.seller.myProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyProductsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is seller My Products Fragment"
    }
    val text: LiveData<String> = _text
}