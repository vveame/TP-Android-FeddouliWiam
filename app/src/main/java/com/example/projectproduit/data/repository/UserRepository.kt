package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class UserRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val userCollection = firestore.collection("users")

    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userCollection.document(user.userId)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userCollection.document(user.userId)
            .update(
                mapOf(
                    "fullName" to user.fullName,
                    "email" to user.email,
                    "phoneNumber" to user.phoneNumber,
                    "address" to user.address,
                    "isVerified" to user.isVerified
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userCollection.document(userId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getUserById(userId: String, onResult: (User?) -> Unit, onError: (Exception) -> Unit) {
        userCollection.document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener { onError(it) }
    }

    fun getAllUsers(onResult: (List<User>) -> Unit, onError: (Exception) -> Unit) {
        userCollection.get()
            .addOnSuccessListener { result ->
                val users = result.documents.mapNotNull { it.toObject(User::class.java) }
                onResult(users)
            }
            .addOnFailureListener { onError(it) }
    }

    fun signUp(user: User, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: user.userId
                val userWithId = user.copy(userId = userId)
                addUser(userWithId, onSuccess, onFailure)
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun signIn(email: String, password: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid
                if (userId != null) {
                    getUserById(userId, onResult = { user ->
                        if (user != null) {
                            onSuccess(user)
                        } else {
                            onFailure(Exception("User document not found"))
                        }
                    }, onError = onFailure)
                } else {
                    onFailure(Exception("User ID not found"))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
    }
}
