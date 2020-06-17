package com.julkar.nain.currencyconverter.main.vm

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.julkar.nain.currencyconverter.database.dao.ExchangeRateDao
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import com.julkar.nain.currencyconverter.util.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


/**
 * Created by Julkar Nain on 6/17/20.
 */
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var communicator: Communicator

    @Mock
    lateinit var exchangeRateDao: ExchangeRateDao

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var observer: Observer<Any>


    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel =
            MainViewModel(context, communicator, ExchangeRatePersistentDataSource(exchangeRateDao))

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun fetchCurrencyDataTest() {
        mainViewModel.fetchCurrencyData()
        runBlocking {
            verify(exchangeRateDao).getExchangeRates()
        }
    }

    @Test
    fun setCurrencyRateTest() {
        setDemoValue()
        mainViewModel.textFrom.value = "10.00"
        mainViewModel.textTo.observeForever(observer)

        mainViewModel.setCurrencyRate(Action.TO, 0)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals("859.50", value)
        }
    }

    @Test
    fun getExchangedAmountTest() {
        setDemoValue()

        val exchangeAmount = mainViewModel.getExchangedAmount(100.00, "USD", "BDT")

        assertEquals(8595.00, exchangeAmount, 0.00)
    }

    @Test
    fun getExchangeRatesTest() {
        setDemoValue()
        val exchangeRates = mainViewModel.getExchangeRates()

        assertEquals(listOf("BDT : 085.95", "USD : 001.00"), exchangeRates)
    }

    @Test
    fun convertRatesTest() {
        setDemoValue()
        mainViewModel.setCurrencyRate(Action.TO, 0)
        mainViewModel.textTo.observeForever(observer)

        mainViewModel.convertRates(Action.FROM, "100")

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals("8,595.00", value)
        }
    }

    private fun setDemoValue() {
        val demoValue = mapOf("BDT" to 85.95, "USD" to 1.00)
        mainViewModel.currencyRatesMap.value = demoValue
    }
}