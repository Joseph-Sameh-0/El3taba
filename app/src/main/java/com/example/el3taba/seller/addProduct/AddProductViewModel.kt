package com.example.el3taba.seller.addProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProductViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is seller Add Product Fragment"
    }
    val text: LiveData<String> = _text
}