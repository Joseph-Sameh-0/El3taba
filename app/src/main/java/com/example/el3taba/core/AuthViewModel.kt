package com.example.el3taba.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> get() = _authState

    // Register user
    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.registerUser(email, password)
                result.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = true // Success
                    } else {
                        _authState.value = false // Failure
                    }
                }
            } catch (e: Exception) {
                _authState.value = false
            }
        }
    }

    // Login user
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                result.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = true // Success
                    } else {
                        _authState.value = false // Failure
                    }
                }
            } catch (e: Exception) {
                _authState.value = false
            }
        }
    }
}
