package com.julkar.nain.currencyconverter.application

import android.app.Application
import com.julkar.nain.currencyconverter.di.AppComponent
import com.julkar.nain.currencyconverter.di.DaggerAppComponent

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
class MainApplication: Application() {
    private var appComponent: AppComponent? = null

    fun getAppComponent(): AppComponent? {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.factory().create(this)
        }
        return appComponent
    }
}