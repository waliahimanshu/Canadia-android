package com.waliahimanshu.canadia.di.module

import com.google.firebase.database.FirebaseDatabase
import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import com.waliahimanshu.canadia.ui.home.ExpressEntryContract
import com.waliahimanshu.canadia.ui.home.ExpressEntryMapper
import com.waliahimanshu.canadia.ui.home.ExpressEntryPresenter
import dagger.Module
import dagger.Provides


/**
 * Module used to provide dependencies at an activity-level.
 */
@Module
open class ExpressEntryActivityModule {

    @PerActivity
    @Provides
    internal fun provideHomeView(expressEntryActivity: ExpressEntryActivity): ExpressEntryContract.View {
        return expressEntryActivity
    }

    @PerActivity
    @Provides
    internal fun provideExpressEntryPresenter(mainView: ExpressEntryContract.View,
                                              mapper: ExpressEntryMapper):
            ExpressEntryContract.Presenter {

        return ExpressEntryPresenter(mainView,
                FirebaseDatabase.getInstance().reference.child("ee_crs"), mapper)
    }

}
