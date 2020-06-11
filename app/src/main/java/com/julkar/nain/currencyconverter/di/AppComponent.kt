package com.julkar.nain.currencyconverter.di

import android.app.Application
import com.julkar.nain.currencyconverter.application.MainApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application?): AppComponent?
    }

    fun inject(mainApplication: MainApplication)
}