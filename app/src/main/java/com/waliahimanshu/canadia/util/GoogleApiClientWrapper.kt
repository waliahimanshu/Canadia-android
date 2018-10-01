package com.waliahimanshu.canadia.util

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.waliahimanshu.canadia.ui.R

class GoogleApiClientWrapper (val context: Context) {

    fun getGoogleApiClient(): GoogleApiClient? {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return GoogleApiClient.Builder(context)
                .enableAutoManage(context as FragmentActivity, (GoogleApiClient.OnConnectionFailedListener {
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()

                }))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)
}
