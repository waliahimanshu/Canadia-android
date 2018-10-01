@file:Suppress("unused")

package com.waliahimanshu.canadia.di.module

import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.login.LoginActivity
import com.waliahimanshu.canadia.ui.login.LoginContract
import com.waliahimanshu.canadia.ui.login.LoginPresenter
import dagger.Module
import dagger.Provides

@Module
interface LoginActivityModule {

    @PerActivity
    @Provides
    fun provideHomeView(loginActivity: LoginActivity): LoginContract.View {
        return loginActivity
    }

    @PerActivity
    @Provides
    fun provideExpressEntryPresenter(view: LoginContract.View): LoginContract.Presenter {
        return LoginPresenter(view)
    }
}
