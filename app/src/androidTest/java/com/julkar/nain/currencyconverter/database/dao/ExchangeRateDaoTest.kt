package com.julkar.nain.currencyconverter.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.julkar.nain.currencyconverter.database.AppDatabase
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Julkar Nain on 6/17/20.
 */
@RunWith(AndroidJUnit4::class)
class ExchangeRateDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var exchangeRateDao: ExchangeRateDao

    @Before
    fun setUp() = runBlocking(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        exchangeRateDao = appDatabase.exchangeRateDao()
        exchangeRateDao.insert(getDemoList())
    }

    @Test
    fun getExchangeRatesTest() = runBlocking {
        val insertedList = exchangeRateDao.getExchangeRates()
        assertEquals(getDemoList().size, insertedList.size)
    }

    @Test
    fun getCountriesNameTest() = runBlocking {
        val countriesName = exchangeRateDao.getCountriesName()
        assertEquals(listOf("BDT", "USD"), countriesName)
    }

    @Test
    fun findExchangeRateByCountryNameTest() = runBlocking {
        val exchangeRateValue = exchangeRateDao.findExchangeRateByCountryName("BDT")
        assertEquals(85.19, exchangeRateValue, 0.0)
    }

    @Test
    fun update() = runBlocking{
        val exchangeRate = ExchangeRate("BDT", 90.78)
        exchangeRateDao.update(exchangeRate)

        val exchangeRateValue = exchangeRateDao.findExchangeRateByCountryName("BDT")
        assertEquals(90.78, exchangeRateValue, 0.0)
    }

    @Test
    fun delete() = runBlocking {
        val exchangeRate = ExchangeRate("BDT", 90.78)
        exchangeRateDao.delete(exchangeRate)
        assertEquals(1, exchangeRateDao.getCount())
    }

    @Test
    fun deleteAll() = runBlocking {
        exchangeRateDao.deleteAll()
        assertEquals(0, exchangeRateDao.getCount())
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    private fun getDemoList(): List<ExchangeRate> {
        val list = mutableListOf<ExchangeRate>()
        list.add(ExchangeRate("BDT", 85.19))
        list.add(ExchangeRate("USD", 1.00))
        return list
    }
}