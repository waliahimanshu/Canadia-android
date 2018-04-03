package com.waliahimanshu.canadia

import android.app.Activity
import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.waliahimanshu.canadia.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class CanadiaApplication : Application(), HasActivityInjector {

    @Inject lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

}
