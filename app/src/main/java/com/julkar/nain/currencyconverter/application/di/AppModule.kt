package com.julkar.nain.currencyconverter.application.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.julkar.nain.currencyconverter.common.ViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@Module
class AppModule {

    companion object{
        val BASE_URL = "http://api.currencylayer.com"
    }

    @Singleton
    @Provides
    fun providesContext(application: Application?): Context {
        return application!!
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}