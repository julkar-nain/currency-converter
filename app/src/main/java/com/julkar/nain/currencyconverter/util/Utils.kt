package com.julkar.nain.currencyconverter.util

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */

@Singleton
class Utils @Inject constructor(){

    //remove "USA" part from all country names
    fun prepareFormattedData(map: MutableMap<String, Double>): Map<String, Double>{
        map[KEY_USA] = 1.0
        return map.mapKeys { it.key.removeRange(0, 3) }
    }
}