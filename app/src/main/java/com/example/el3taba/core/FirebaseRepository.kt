package com.example.el3taba.core

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    // Register new user
    suspend fun registerUser(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    // Login user
    suspend fun loginUser(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    // Fetch Firestore data
    suspend fun getUserData(uid: String): Task<DocumentSnapshot> {
        return firestore.collection("users").document(uid).get()
    }

    // More Firebase methods (e.g., logout, reset password, etc.)
}
