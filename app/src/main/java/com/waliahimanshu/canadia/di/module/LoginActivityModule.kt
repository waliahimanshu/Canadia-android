@file:Suppress("unused")

package com.waliahimanshu.canadia.di.module

import com.google.firebase.auth.FirebaseAuth
import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.login.*
import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {

    @PerActivity
    @Provides
    fun provideLoginActivityView(loginActivity: LoginActivity): LoginContract.View {
        return loginActivity
    }

    @PerActivity
    @Provides
    fun provideLoginActivityPresenter(view: LoginActivity): LoginContract.Presenter {
        return LoginPresenter(view, GoogleApiClientWrapper(view), FirebaseWrapper(FirebaseAuth.getInstance()))
    }
}
