package com.waliahimanshu.canadia.di.component

import android.app.Application
import com.waliahimanshu.canadia.CanadiaApplication
import com.waliahimanshu.canadia.di.module.ActivityBindingModule
import com.waliahimanshu.canadia.di.module.ApplicationModule
import com.waliahimanshu.canadia.di.module.WalkthroughActivityBinding
import com.waliahimanshu.canadia.di.scopes.PerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@PerApplication
@Component(modules = [(ActivityBindingModule::class),
    (WalkthroughActivityBinding::class),
    (ApplicationModule::class),
    (AndroidSupportInjectionModule::class)])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: CanadiaApplication)
}
