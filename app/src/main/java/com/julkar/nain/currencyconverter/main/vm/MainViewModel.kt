package com.julkar.nain.currencyconverter.main.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import com.julkar.nain.currencyconverter.repository.ExchangeRateNetworkDataSource
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainViewModel constructor(
    private val exchangeRateNetworkDataSource: ExchangeRateNetworkDataSource,
    private val exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource
) :
    ViewModel() {

    private val TAG = javaClass.toString()
    private val KEY_USA = "USDUSD"

    private val currencyRatesMap = MutableLiveData<Map<String, Double>>()
    private val compositeDisposable = CompositeDisposable()

    fun fetchCurrencyData(): LiveData<Map<String, Double>> {
        viewModelScope.launch(Dispatchers.IO) {
            val rates =  exchangeRatePersistentDataSource.getExchangeRates()
            currencyRatesMap.postValue(rates.map { it.countryName to it.exchangeRate }.toMap())
        }

        return currencyRatesMap
    }

    fun getCurrencyRate(position: Int): String {
        return currencyRatesMap.value?.keys?.elementAt(position)!!
    }

    fun getExchangedAmount(unExchanged: Double, from: String, to: String): Double {
        val exchangedAmount =
            (1 / currencyRatesMap.value!![from]!!) * currencyRatesMap.value!![to]!! * unExchanged

        return exchangedAmount
    }

    fun getExchangeRates(): List<String> {
        return currencyRatesMap.value?.map { it.key + " : " + DecimalFormat("000.00").format(it.value) }
            ?.toList()!!
    }

    fun saveExchangeRates(ratesMap: Map<String, Double>) = viewModelScope.launch(Dispatchers.IO) {
        val rateList = ratesMap.map {
            ExchangeRate(it.key, it.value)
        }.toList()

        exchangeRatePersistentDataSource.saveExchangeRates(rateList)
    }

    fun fetchNetworkData() {
        compositeDisposable.add(
            exchangeRateNetworkDataSource.getExchangeRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val map = it.quotes
                    map[KEY_USA] = 1.0
                    val formattedMap = map.mapKeys { it.key.removeRange(0, 3) }
                    saveExchangeRates(formattedMap)
                }, {
                    Log.d(TAG, it.toString())
                })
        )
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}