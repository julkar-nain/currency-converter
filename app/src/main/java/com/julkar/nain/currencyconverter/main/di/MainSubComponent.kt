package com.julkar.nain.currencyconverter.main.di

import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.main.MainActivity
import dagger.Subcomponent

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@ActivityScope
@Subcomponent(modules = [MainModule::class ])
interface MainSubComponent {

    fun inject(mainActivity: MainActivity?)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainSubComponent
    }
}