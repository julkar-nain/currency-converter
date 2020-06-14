package com.julkar.nain.currencyconverter.application.di

import android.app.Application
import androidx.work.CoroutineWorker
import com.julkar.nain.currencyconverter.application.MainApplication
import com.julkar.nain.currencyconverter.main.di.MainSubComponent
import com.julkar.nain.currencyconverter.service.scheduler.DataWorker
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

    fun inject(mainApplication: MainApplication)
    fun inject(worker: DataWorker)
    fun getMainSubComponent(): MainSubComponent.Factory?

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application?): AppComponent?
    }
}