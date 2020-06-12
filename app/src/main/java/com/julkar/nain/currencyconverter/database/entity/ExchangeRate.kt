package com.julkar.nain.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Entity(tableName = "exchange_rate_table")
class ExchangeRate(
    @PrimaryKey @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "exchange_rate") val exchangeRate: String
)