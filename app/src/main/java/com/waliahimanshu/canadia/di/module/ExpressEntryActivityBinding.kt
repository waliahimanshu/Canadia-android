package com.waliahimanshu.canadia.di.module

import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import com.waliahimanshu.canadia.ui.login.LoginActivity
import com.waliahimanshu.canadia.ui.walkthrough.WalkthroughActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface ActivityBuilder {
    @PerActivity
    @ContributesAndroidInjector(modules = [(ExpressEntryActivityModule::class)])
    fun bindMainActivity(): ExpressEntryActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [])
    fun bindWalkthroughActivity(): WalkthroughActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    fun bindLoginActivity() : LoginActivity
}
