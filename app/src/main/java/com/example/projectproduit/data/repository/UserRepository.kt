package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val userCollection = firestore.collection("users")

    suspend fun updateUser(user: User) = suspendCancellableCoroutine<Unit> { cont ->
        userCollection.document(user.userId)
            .update(
                mapOf(
                    "fullName" to user.fullName,
                    "email" to user.email,
                    "phoneNumber" to user.phoneNumber,
                    "address" to user.address,
                    "role" to user.role.name
                )
            )
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun getUserById(userId: String): User? = suspendCoroutine { cont ->
        userCollection.document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                cont.resume(user)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun getAllUsers(): List<User> = suspendCoroutine { cont ->
        userCollection.get()
            .addOnSuccessListener { result ->
                val users = result.documents.mapNotNull { it.toObject(User::class.java) }
                cont.resume(users)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun signUp(user: User, password: String): User = suspendCoroutine { cont ->
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: user.userId
                val userWithId = user.copy(userId = userId)
                userCollection.document(userWithId.userId)
                    .set(userWithId)
                    .addOnSuccessListener { cont.resume(userWithId) }
                    .addOnFailureListener { cont.resumeWithException(it) }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun signIn(email: String, password: String): User = suspendCoroutine { cont ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid
                if (userId != null) {
                    userCollection.document(userId)
                        .get()
                        .addOnSuccessListener { doc ->
                            val user = doc.toObject(User::class.java)
                            if (user != null) cont.resume(user)
                            else cont.resumeWithException(Exception("User document not found"))
                        }
                        .addOnFailureListener { cont.resumeWithException(it) }
                } else {
                    cont.resumeWithException(Exception("User ID not found"))
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    fun signOutUser() {
        firebaseAuth.signOut()
    }
}
