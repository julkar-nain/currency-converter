package com.julkar.nain.currencyconverter.main.di

import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.service.CurrencyRatesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@Module
class MainModule {

    @ActivityScope
    @Provides
    open fun provideRetrofitService(retrofit: Retrofit): CurrencyRatesApi? {
        return retrofit.create<CurrencyRatesApi>(CurrencyRatesApi::class.java)
    }
}