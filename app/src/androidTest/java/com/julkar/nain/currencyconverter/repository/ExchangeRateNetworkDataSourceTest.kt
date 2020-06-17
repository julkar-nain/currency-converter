package com.julkar.nain.currencyconverter.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.julkar.nain.currencyconverter.application.di.AppModule
import com.julkar.nain.currencyconverter.domain.CurrencyRatesResponse
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import com.julkar.nain.currencyconverter.service.Communicator.CommunicatorImp
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import io.reactivex.internal.operators.flowable.FlowableConcatMap.subscribe
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Julkar Nain on 6/17/20.
 */
@RunWith(AndroidJUnit4::class)
class ExchangeRateNetworkDataSourceTest {

    private lateinit var retrofit: Retrofit
    private lateinit var exchangeRateNetworkDataSource: ExchangeRateNetworkDataSource
    @Mock
    lateinit var observer: Observer<Any>

    @Before
    fun setUp() {
        retrofit = Retrofit.Builder().baseUrl(AppModule.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        exchangeRateNetworkDataSource = ExchangeRateNetworkDataSource(retrofit.create(CurrencyRatesService::class.java))
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getExchangeRatesTest() {
         val testObserver = TestObserver.create<CurrencyRatesResponse>()
        exchangeRateNetworkDataSource.getExchangeRates().subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0) {
            it.quotes.isNotEmpty()
        }
    }
}