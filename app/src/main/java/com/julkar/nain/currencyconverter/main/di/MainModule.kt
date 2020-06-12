package com.julkar.nain.currencyconverter.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.julkar.nain.currencyconverter.adapter.CurrencyRatesAdapter
import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.common.ViewModelFactory
import com.julkar.nain.currencyconverter.common.vm.ViewModelKey
import com.julkar.nain.currencyconverter.main.vm.MainViewModel
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
    fun providesMainViewModel(currencyRatesService: CurrencyRatesService): ViewModel {
        return MainViewModel(currencyRatesService)
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
}