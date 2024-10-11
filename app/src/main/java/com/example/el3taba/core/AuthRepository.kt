package com.example.el3taba.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.MutableLiveData
import com.example.el3taba.core.dataClasses.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Sign Up function
    fun signUp(email: String, password: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }

        return result
    }

    // Save User Role function
    fun saveUserRole(user: User): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid

        userId?.let {
            db.collection("users").document(it)
                .set(user)
                .addOnCompleteListener { task ->
                    result.value = task.isSuccessful
                }
        } ?: run {
            result.value = false
        }

        return result
    }
    // Function for Login
    fun login(email: String, password: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }

        return result
    }

    // Function for Password Reset
    fun resetPassword(email: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }

        return result
    }

    // Function to get the current user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Function to sign out the user
    fun signOut() {
        auth.signOut()
    }

}
