package com.julkar.nain.currencyconverter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.julkar.nain.currencyconverter.database.dao.ExchangeRateDao
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import com.julkar.nain.currencyconverter.util.DATABASE_VERSION

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Database(entities = [ExchangeRate::class], version = DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exchangeRateDao(): ExchangeRateDao
}