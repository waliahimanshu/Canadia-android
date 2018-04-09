package com.waliahimanshu.canadia.di.module

import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import com.waliahimanshu.canadia.ui.walkthrough.WalkthroughActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [(ExpressEntryActivityModule::class)])
    abstract fun bindMainActivity(): ExpressEntryActivity

}

@Module
abstract class WalkthroughActivityBinding {

    @PerActivity
    @ContributesAndroidInjector(modules = [])
    abstract fun bindWalkthroughActivity(): WalkthroughActivity

}