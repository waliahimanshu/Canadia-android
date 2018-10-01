package com.waliahimanshu.canadia.util

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseWrapper(private val firebaseAuth: FirebaseAuth) {

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        return firebaseAuth.signInWithCredential(credential)
    }
}
