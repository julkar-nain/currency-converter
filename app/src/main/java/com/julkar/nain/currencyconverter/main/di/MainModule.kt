package com.julkar.nain.currencyconverter.main.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.julkar.nain.currencyconverter.adapter.CurrencyRatesAdapter
import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.common.ViewModelFactory
import com.julkar.nain.currencyconverter.common.vm.ViewModelKey
import com.julkar.nain.currencyconverter.main.vm.MainViewModel
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
@Module
class MainModule {

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
    fun providesAdapter(): CurrencyRatesAdapter {
        return CurrencyRatesAdapter()
    }
}