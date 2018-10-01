package com.waliahimanshu.canadia.ui.login

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult.NETWORK_ERROR
import com.google.android.gms.common.api.GoogleApiClient
import javax.inject.Inject

private const val RC_SIGN_IN = 1
private val tag = LoginPresenter::class.java.simpleName

class LoginPresenter @Inject constructor(private val view: LoginContract.View,
                                         private val wrapper: GoogleApiClientWrapper,
                                         private val firebaseWrapper: FirebaseWrapper) : LoginContract.Presenter {


    // state/field in presenter???
    private var googleApiClient: GoogleApiClient? = null

    override fun onStart() {
        val account = wrapper.getLastSignedInAccount()
        if (account != null) {
            view.launchMainActivity()
        }
    }

    override fun init() {
        googleApiClient = wrapper.getGoogleApiClient()
    }

    override fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        view.startActivityForResult(signInIntent)
    }


    override fun handleGoogleSignInResult(data: Intent?, requestCode: Int) {
        lateinit var result: GoogleSignInResult
        if (requestCode == RC_SIGN_IN) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

        }
        if (result.isSuccess) {
            val account = result.signInAccount
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } else {
            if (result.status.statusCode == NETWORK_ERROR) {
                Log.e(tag, "Google sign in failed : status code{${result.status}} ")
                view.showNetworkFailureError()
            } else {
                Log.e(tag, "Google sign in failed : status code{${result.status}} ")
                view.showGenericError()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        firebaseWrapper.firebaseAuthWithGoogle(account).addOnCompleteListener {
            if (it.isSuccessful) {
                view.launchMainActivity()
            } else {
                Log.e(tag, "Firebase error occurred {${it.exception}} ")
                view.showGenericError()
            }
        }
    }
}

