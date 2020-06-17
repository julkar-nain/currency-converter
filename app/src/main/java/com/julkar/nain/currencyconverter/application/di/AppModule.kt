package com.julkar.nain.currencyconverter.application.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.julkar.nain.currencyconverter.database.AppDatabase
import com.julkar.nain.currencyconverter.database.dao.ExchangeRateDao
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.julkar.nain.currencyconverter.service.Communicator.CommunicatorImp as CommunicatorImp1


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

    @Singleton
    @Provides
    fun providesAppDatabase(application: Application?): AppDatabase {
            return Room.databaseBuilder<AppDatabase>(application!!, AppDatabase::class.java, "app-database")
                .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): CurrencyRatesService {
        return retrofit.create(CurrencyRatesService::class.java)
    }

    @Singleton
    @Provides
    fun providesExchangeRateDao(appDatabase: AppDatabase): ExchangeRateDao {
        return appDatabase.exchangeRateDao()
    }

    @Singleton
    @Provides
    fun providesCommunicator(): Communicator = CommunicatorImp1()
}