package com.waliahimanshu.canadia.ui.login

import android.content.Intent

interface LoginContract {

    interface Presenter {
        fun signIn()
        fun onStart()
        fun init()

    }

    interface View {
        fun launchMainActivity()
        fun startActivityForResult(signInIntent: Intent?)
    }
}
