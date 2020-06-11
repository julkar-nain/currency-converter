package com.julkar.nain.currencyconverter.main.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import com.julkar.nain.currencyconverter.util.Constants.ACCESS_KEY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel constructor(private val currencyRatesService: CurrencyRatesService) :
    ViewModel() {

    private val TAG = javaClass.toString()

    private val currencyRatesMap = MutableLiveData<Map<String, Double>>()
    private val compositeDisposable = CompositeDisposable()

    fun fetchCurrencyData(): LiveData<Map<String, Double>> {
        compositeDisposable.add(
            currencyRatesService.getCurrencyRates(ACCESS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currencyRatesMap.value = it.quotes.mapKeys { it.key.removeRange(0, 3) }
                }, {
                    Log.d(TAG, it.toString())
                })
        )

        return currencyRatesMap
    }

    fun getCurrencyRate(position: Int) : String{
        return currencyRatesMap.value?.keys?.elementAt(position)!!
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}