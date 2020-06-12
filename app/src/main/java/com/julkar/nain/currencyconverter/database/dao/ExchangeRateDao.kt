package com.julkar.nain.currencyconverter.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single


/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Dao
interface ExchangeRateDao {
    @Query("SELECT * from exchange_rate_table")
    suspend fun getExchangeRates(): List<ExchangeRate>

    @Query("SELECT country_name FROM exchange_rate_table")
    fun getCountriesName(): LiveData<List<String>>

    @Query("SELECT * FROM exchange_rate_table WHERE country_name = :countryName")
    fun findExchangeRateByCountryName(countryName: String): LiveData<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRates: List<ExchangeRate>)

    @Update
    suspend fun update(exchangeRate: ExchangeRate)

    @Delete
    suspend fun delete(exchangeRate: ExchangeRate)

    @Query("DELETE FROM exchange_rate_table")
    suspend fun deleteAll()
}