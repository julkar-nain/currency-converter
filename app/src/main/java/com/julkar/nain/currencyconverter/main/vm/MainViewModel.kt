package com.julkar.nain.currencyconverter.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import com.julkar.nain.currencyconverter.util.Constants.ACCESS_KEY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(val currencyRatesService: CurrencyRatesService) : ViewModel() {

    private val currencyRates = MutableLiveData<List<String>>()
    private lateinit var currencyRatesMap: Map<String, Double>
    private lateinit var compositeDisposable: CompositeDisposable

    fun fetchCurrencyData(): LiveData<List<String>> {
        compositeDisposable.add(
            currencyRatesService.getCurrencyRates(ACCESS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currencyRatesMap = it.quotes
                    currencyRates.value = currencyRatesMap.keys.toList()
                }, {
                })
        )

        return currencyRates
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}