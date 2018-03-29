package com.waliahimanshu.canadia.di.module

import com.waliahimanshu.canadia.di.scopes.PerActivity
import com.waliahimanshu.canadia.ui.home.*
import dagger.Module
import dagger.Provides


/**
 * Module used to provide dependencies at an activity-level.
 */
@Module
open class ExpressEntryActivityModule {

    @PerActivity
    @Provides
    internal fun provideHomeView(expresEntryActivity: ExpressEntryActivity): ExpressEntryContract.View {
        return expresEntryActivity
    }

    @PerActivity
    @Provides
    internal fun provideExpressEntryPresenter(mainView: ExpressEntryContract.View,
                                              expressEntryModel: ExpressEntryModel, mapper: ExpressEntryMapper):
            ExpressEntryContract.Presenter {
        return ExpressEntryPresenter(mainView, expressEntryModel, mapper)
    }

}
