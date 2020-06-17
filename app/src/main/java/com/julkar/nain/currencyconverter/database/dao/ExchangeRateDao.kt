package com.julkar.nain.currencyconverter.database.dao

import androidx.room.*
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate


/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Dao
interface ExchangeRateDao {

    @Query("SELECT * from exchange_rate_table order by country_name ASC")
    suspend fun getExchangeRates(): List<ExchangeRate>

    @Query("SELECT country_name FROM exchange_rate_table order by country_name ASC")
    suspend fun getCountriesName(): List<String>

    @Query("SELECT exchange_rate FROM exchange_rate_table WHERE country_name = :countryName")
    suspend fun findExchangeRateByCountryName(countryName: String): Double

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRates: List<ExchangeRate>)

    @Update
    suspend fun update(exchangeRate: ExchangeRate)

    @Delete
    suspend fun delete(exchangeRate: ExchangeRate)

    @Query("DELETE FROM exchange_rate_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM exchange_rate_table")
    suspend fun getCount(): Int
}