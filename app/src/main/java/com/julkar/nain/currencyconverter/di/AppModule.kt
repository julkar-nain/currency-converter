package com.julkar.nain.currencyconverter.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@Module
class AppModule {
    @Singleton
    @Provides
    fun getContext(application: Application?): Context? {
        return application
    }
}