package com.waliahimanshu.canadia.ui.login

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient


class LoginPresenter(private val view: LoginContract.View, private val wrapper: GoogleApiClientWrapper) : LoginContract.Presenter {
    // state/field in presenter???
    private var googleApiClient: GoogleApiClient? = null


    override fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        view.startActivityForResult(signInIntent)
    }

    override fun init() {
        googleApiClient = wrapper.getGoogleApiClient()

    }

    override fun onStart() {
        val account = wrapper.getLastSignedInAccount()
        if (account != null) {
            view.launchMainActivity()
        }
    }
}

