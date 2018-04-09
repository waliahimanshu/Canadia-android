package com.waliahimanshu.canadia.di.component

import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ExpressEntryActivitySubComponent : AndroidInjector<ExpressEntryActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ExpressEntryActivity>()

}