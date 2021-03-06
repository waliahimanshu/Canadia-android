package com.waliahimanshu.canadia.di.module

import android.app.Application
import android.content.Context
import com.waliahimanshu.canadia.di.scopes.PerApplication
import com.waliahimanshu.canadia.util.PreferencesHelper
import dagger.Module
import dagger.Provides

/**
 * Module used to provide dependencies at an application-level.
 */
@Module
open class ApplicationModule {

    @Provides
    @PerApplication
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @PerApplication
    internal fun providePreferencesHelper(context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }
}
