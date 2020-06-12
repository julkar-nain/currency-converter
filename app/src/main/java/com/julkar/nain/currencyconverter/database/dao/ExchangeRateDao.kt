package com.julkar.nain.currencyconverter.database.dao

import androidx.room.*
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Dao
interface ExchangeRateDao {
    @Query("SELECT * from exchange_rate_table")
    fun getExchangeRates(): List<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRates: List<ExchangeRate>)

    @Update
    suspend fun update(exchangeRate: ExchangeRate)

    @Delete
    suspend fun delete(exchangeRate: ExchangeRate)

    @Query("DELETE FROM exchange_rate_table")
    suspend fun deleteAll()
}