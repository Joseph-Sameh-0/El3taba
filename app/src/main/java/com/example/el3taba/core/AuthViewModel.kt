package com.example.el3taba.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.el3taba.core.dataClasses.User
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    fun login(email: String, password: String) = repository.login(email, password)
    fun resetPassword(email: String) = repository.resetPassword(email)
    // Sign up method
    fun signUp(email: String, password: String): MutableLiveData<Boolean> {
        return repository.signUp(email, password)
    }

    // Save user role method
    fun saveUserRole(user: User): MutableLiveData<Boolean> {
        return repository.saveUserRole(user)
    }
    fun getUserRole(uid: String): LiveData<String?> {
        val result = MutableLiveData<String?>()
        val userRef = FirebaseFirestore.getInstance().collection("users").document(uid)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val role = document.getString("role")
                result.value = role
            } else {
                Log.d("com.example.el3taba.core.AuthViewModel", "No such document")
                result.value = null
            }
        }.addOnFailureListener { e ->
            Log.w("com.example.el3taba.core.AuthViewModel", "Error getting user role", e)
            result.value = null
        }

        return result
    }

    fun changePassword(currentPassword: String, newPassword: String): MutableLiveData<Boolean> {
        return repository.changePassword(currentPassword, newPassword)
    }


}
