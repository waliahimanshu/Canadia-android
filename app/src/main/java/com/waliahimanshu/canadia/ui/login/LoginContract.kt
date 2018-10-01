package com.waliahimanshu.canadia.ui.login

import android.content.Intent

interface LoginContract {

    interface Presenter {
        fun signIn()
        fun onStart()
        fun init()
        fun handleGoogleSignInResult(data: Intent?, requestCode: Int)
    }

    interface View {
        fun launchMainActivity()
        fun startActivityForResult(signInIntent: Intent?)
        fun showNetworkFailureError()
        fun showGenericError()
    }
}
