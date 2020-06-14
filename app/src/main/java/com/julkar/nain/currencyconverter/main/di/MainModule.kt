package com.julkar.nain.currencyconverter.main.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.julkar.nain.currencyconverter.adapter.CurrencyRatesAdapter
import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.common.ViewModelFactory
import com.julkar.nain.currencyconverter.common.vm.ViewModelKey
import com.julkar.nain.currencyconverter.database.AppDatabase
import com.julkar.nain.currencyconverter.database.dao.ExchangeRateDao
import com.julkar.nain.currencyconverter.main.vm.MainViewModel
import com.julkar.nain.currencyconverter.repository.ExchangeRateNetworkDataSource
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): CurrencyRatesService {
        return retrofit.create<CurrencyRatesService>(CurrencyRatesService::class.java)
    }

    @ActivityScope
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Provides
    fun providesMainViewModel(
        context: Context,
        communicator: Communicator,
        exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource
    ): ViewModel {
        return MainViewModel(context, communicator, exchangeRatePersistentDataSource)
    }

    @ActivityScope
    @Provides
    fun providesViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
        return factory
    }

    @ActivityScope
    @Provides
    fun providesAdapter(): CurrencyRatesAdapter{
        return CurrencyRatesAdapter()
    }

    @ActivityScope
    @Provides
    fun providesExchangeRateDao(appDatabase: AppDatabase): ExchangeRateDao{
        return appDatabase.exchangeRateDao()
    }
}